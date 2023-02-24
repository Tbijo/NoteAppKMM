import SwiftUI

struct HideableSearchTextField<Destination: View>: View {

    // passable parameters from outside of this view

    // get new text
    var onSearchToggled: () -> Void

    // we want to pass a view to this view
    // when we click on + btn in the toolbar we navigate
    // we want to specify where to navigate from the outside when click on + btn
    // To do it we specify a generic <Destination: View> so a Destination of type View
    // So when we click on the + we will navigate to tha Destination
    var destinationProvider: () -> Destination

    var isSearchActive: Bool

    // @Binding - two way binding
    // We assign a value to a TextField (the text)
    // and we get a lambda that tells us when the text changes
    // in that lambda we update our text which reflects the state of TextField
    // View is allowed to change this var even if it is not in its scope
    // We do not need to create the logic that updates text on every user type input
    @Binding var searchText: String
    
    var body: some View {
        // HStack - Horizontal Stack (Row)
        HStack {
            // Hint... , value itself
            TextField("Search...", text: $searchText)
                // not need to specify the object just use .
                .textFieldStyle(.roundedBorder)
                // alpha value, setting visibility
                // do not remove view because other views might shrink
                .opacity(isSearchActive ? 1 : 0)

            if !isSearchActive {
                // if search is inactive spacer will occupy the remaining space instead of TextField
                Spacer()
            }

            // Search Icon button
            Button(action: onSearchToggled) {
                Image(systemName: isSearchActive ? "xmark" : "magnifyingglass")
                    .foregroundColor(.black)
            }

            // Add (+) button
            NavigationLink(destination: destinationProvider()) {
                Image(systemName: "plus")
                    .foregroundColor(.black)
            }
        }
    }
}

// Just for previews
struct HideableSearchTextField_Previews: PreviewProvider {
    static var previews: some View {
        HideableSearchTextField(
            onSearchToggled: {},
            destinationProvider: { EmptyView() },
            isSearchActive: true,
            searchText: .constant("YouTube")
        )
    }
}
