package com.plcoding.noteappkmm.android.note_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.noteappkmm.domain.note.Note
import com.plcoding.noteappkmm.domain.note.NoteDataSource
import com.plcoding.noteappkmm.domain.note.SearchNotes
import com.plcoding.noteappkmm.domain.time.DateTimeUtil
import com.plcoding.noteappkmm.presentation.RedOrangeHex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

// It is possible to share ViewModels but it is more complicated
// in this app we wont which easier but we will have to write more code

// shared viewModel makes sense when we use StateFlow
// when we have something shared we can directly observe Android and iOS

// Flows work with KMM but needs more setUp we require (custom dispatchers for iOS, custom Flow type)
// this project will be simple

// Hilt is in Java so we can not use it shared nor iOS module we do not have to
// Automated injection framework are not a thing in iOS
// We will go with the manual approach using constructor injecting
// So if we want to scope a viewModel to a view we just create it in that view and view is destroyed the viewModel will be as well.
// If we want a SINGLETON we will declare these dependencies in the Application class.
// There is a similar application class in iOS like in Android.

// In Android we should use Hilt to avoid ViewModel Factories, easily inject SavedStateHandle.
// In iOS do it rather on your own.

// Koin
// Advantage is you can use it in the shared dir in commonMain.
// But it is not necessary.
// Does not provide the proper injection of SavedStateHandle and navArgs. (deprecated)


@HiltViewModel
class NoteListViewModel @Inject constructor(
    // abstract dao interface
    private val noteDataSource: NoteDataSource,
    // contains navigation arguments, data survives process death
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    // reference to our UseCase
    // does not need params so just inits here otherwise should be in constructor
    private val searchNotes = SearchNotes()

    // make states survive proc death (not necessary)
    // these states are not in one big state which we can pass on process death. it is because we only these specifically to survive and not the whole state
    // we can make the NoteListState to a nav argument by making it a parcelable
    // Some states should be restored to a default value on process death (ex. connection to API: boolean)
    private val notes = savedStateHandle.getStateFlow("notes", emptyList<Note>())
    private val searchText = savedStateHandle.getStateFlow("searchText", "")
    // initial value should be false to keep search field not toggled
    private val isSearchActive = savedStateHandle.getStateFlow("isSearchActive", false)

    // we can still construct our NoteListState with combine
    val state = combine(notes, searchText, isSearchActive) { notes, searchText, isSearchActive ->
        // this block will run anytime any of these three statesFlows emits a value

        // we want to change state anytime one of the above stateFlow changes
        NoteListState(
            // we need to make sure whether the user is searching, if searchText is empty we get all notes
            notes = searchNotes.execute(notes, searchText),
            searchText = searchText,
            isSearchActive = isSearchActive
        )
        // state is now just a Flow
        // does nothing we need to either launch() it or make it a StateFlow
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteListState())
    // stateIn converts it to a StateFlow (it will cache the latest value)
    // this resulting stateFlow will only emit a value if the result of combine() changes (combine will fire more times than the StateFlow will emit values)

    // SharingStarted.WhileSubscribed(5000)
    // means this block will be executed only when there are active subscribers to the resulting StateFlow
    // 5000 on screen rotation we wont resubscribe to the StateFlow, we will resubscribe when the user leaves the app for more than 5 seconds

    fun loadNotes() {
        viewModelScope.launch {
            // we just update the value in savedStateHandle which will make it be saved on proc death
            // when we update it the StateFlow at declaration will Fire and trigger the combine block
            savedStateHandle["notes"] = noteDataSource.getAllNotes()
        }
    }

    fun onSearchTextChange(text: String) {
        // change search text
        savedStateHandle["searchText"] = text
    }

    fun onToggleSearch() {
        // change visibility of search
        savedStateHandle["isSearchActive"] = !isSearchActive.value
        if(!isSearchActive.value) {
            // if search is not visible clear text
            savedStateHandle["searchText"] = ""
        }
    }

    fun deleteNoteById(id: Long) {
        viewModelScope.launch {
            noteDataSource.deleteNoteById(id)
            // after deleting reload other notes
            // normally this wouldnt be necessary if we would return a Flow from SQLDelight DB
            // the change in DB would trigger the loading
            // We chose a easier way that is why we need to call loading manually
            loadNotes()
        }
    }
}