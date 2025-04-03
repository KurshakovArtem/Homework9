package data

data class Message(
    var id: Int,                       // Идентификатор сообщения
    val message: String,               // Сообщение
    val authorId: Int = 1,             // Идентификатор автора сообщения (1 если от пользователя чата)
    val author: String = "Author",     // Автор сообщения (по умолчанию пользователь чата)
    var isDelete: Boolean = false,     // Значение true если удалён
    var isRead: Boolean = false        // Значение true если сообщение прочитано
)
