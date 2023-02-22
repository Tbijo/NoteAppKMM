package com.plcoding.noteappkmm.android.note_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListScreen(
    navController: NavController,
    viewModel: NoteListViewModel = hiltViewModel()
) {
    // transferring StateFlow into composeState
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        // effect will launch on first compose and on recompose when we will return from details
        // that is why do not put loadNotes() in init{} in ViewModel (which would not reload data)
        viewModel.loadNotes()
    }

    // scaffold for floating action button
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("note_detail/-1L")
                },
                backgroundColor = Color.Black
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add note",
                    tint = Color.White
                )
            }
        }
    // padding from scaffold if it resizes or it wants to apply padding to its child
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                HideableSearchTextField(
                    text = state.searchText,
                    isSearchActive = state.isSearchActive,
                    onTextChange = viewModel::onSearchTextChange,
                    onSearchClick = viewModel::onToggleSearch,
                    onCloseClick = viewModel::onToggleSearch,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                )
                // it needs a clear reference to a Column
                this@Column.AnimatedVisibility(
                    visible = !state.isSearchActive,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = "All notes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                }
            }
            LazyColumn(
                // we want to occupy the remaining space, we did not specify weight nowhere else
                modifier = Modifier.weight(1f)
            ) {
                items(
                    items = state.notes,
                    // for the animation of our list we must specify a key
                    // so that the list knows what to update and animate
                    key = { it.id!! }
                ) { note ->
                    NoteItem(
                        note = note,
                        backgroundColor = Color(note.colorHex),
                        onNoteClick = {
                            navController.navigate("note_detail/${note.id}")
                        },
                        onDeleteClick = {
                            viewModel.deleteNoteById(note.id!!)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            // animate changes in our list
                            .animateItemPlacement()
                    )
                }
            }
        }
    }
}