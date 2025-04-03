import data.Chat
import data.Message
import service.ChatService

fun main() {
    val chatService = ChatService
    val chat = Chat(3, 3, mutableListOf(Message(1, "Проверочное сообщение")))
    chatService.addChat(chat)
    chatService.addChat(
        chat.copy(
            companionId = 2, messages = mutableListOf(
                Message(
                    5, "Сообщение 2", 2
                )
            )
        )
    )
    chatService.addChat(chat.copy(messages = mutableListOf(Message(5, "Сообщение 6", 4))))
    chatService.addMessage(
        1, Message(
            5, "Сообщение 7", 5, isRead = true, isDelete = true
        )
    )
    println(chatService.returnChats())
    //println(chatService.deleteChat(2))
    //chatService.deleteChat(2)

    chatService.addMessage(1, Message(5, "Сообщение 3", 2))
    chatService.addMessage(2, Message(5, "Сообщение 4", 1))


    chatService.deleteMessage(4, 1)
    println(chatService.returnChats())
    chatService.editMessage(2, Message(3, "Сообщение 5", 1))

    println(chatService.returnChats())
    println(chatService.getUnreadChatsCount())

    println(chatService.getMessageByChatId(4))

    println(chatService.getMessageByCountAndAuthorId(3, 2))

}