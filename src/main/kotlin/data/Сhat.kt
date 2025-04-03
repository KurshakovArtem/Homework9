package data

data class Chat(
    val id: Int,                                           // Идентификатор чата
    val companionId: Int,                                  // Идентификатор собеседника
    var messages: MutableList<Message> = mutableListOf(),  // Список сообщений
    var isDelete: Boolean = false                          // Значение true если чат удалён
)
