//
//  HideableSearchTextField.swift
//  iosApp
//
//  Created by Philipp Lackner on 26.09.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct HideableSearchTextField<Destination: View>: View {

    // passable parameters from outside of this view

    // get new text
    var onSearchToggled: () -> Void

    // we want to pass a view to this view
    // when we click on + btn in the toolbar we navigate
    // we want to specify where to navigate from the outside when click on + btn
    var destinationProvider: () -> Destination

    var isSearchActive: Bool

    @Binding var searchText: String
    
    var body: some View {
        HStack {
            TextField("Search...", text: $searchText)
                .textFieldStyle(.roundedBorder)
                .opacity(isSearchActive ? 1 : 0)
            if !isSearchActive {
                Spacer()
            }
            Button(action: onSearchToggled) {
                Image(systemName: isSearchActive ? "xmark" : "magnifyingglass")
                    .foregroundColor(.black)
            }
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
