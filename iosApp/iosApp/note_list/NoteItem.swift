import SwiftUI
import shared

struct NoteItem: View {
    var note: Note
    var onDeleteClick: () -> Void
    
    var body: some View {
        
        // VStack - Vertical Stack (Column)
        // alignment: .leading - alingned to left (start)
        VStack(alignment: .leading) {
            HStack {
                Text(note.title)
                    .font(.title3)
                    .fontWeight(.semibold)

                // Spacer pushes both views appart from eachother
                Spacer()

                Button(action: onDeleteClick) {
                    Image(systemName: "xmark").foregroundColor(.black)
                }
            
            // bottom padding is equal to 3
            }.padding(.bottom, 3)
            
            Text(note.content)
                .fontWeight(.light)
                .padding(.bottom, 3)
            
            HStack {
                Spacer()
                // DateTimeUtil().formatNoteDate - iOS doesnt know objects so we need to make an instance
                Text(DateTimeUtil().formatNoteDate(dateTime: note.created))
                    .font(.footnote)
                    .fontWeight(.light)
            }
        }
        .padding()
        // We created a custom constructor for generating color from Long (int64)
        .background(Color(hex: note.colorHex))
        .clipShape(RoundedRectangle(cornerRadius: 5.0))
    }
}

struct NoteItem_Previews: PreviewProvider {
    static var previews: some View {
        NoteItem(
            note: Note(id: nil, title: "My note", content: "Note content", colorHex: 0xFF2341, created: DateTimeUtil().now()),
            onDeleteClick: {}
        )
    }
}
