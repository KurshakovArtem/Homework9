package service

import data.*
import exceptions.*
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

class ChatServiceTest {
    @Before
    fun clearBeforeTest() {
        ChatService.clear()
    }

    @Test
    fun createCorrectAddChat() {
        val chatService = ChatService
        chatService.addChat(Chat(5, 2, mutableListOf(Message(5, "Проверочное сообщение"))))
        assertEquals(
            Chat(
                1, 2, isDelete = false, messages = mutableListOf(
                    Message(
                        1, "Проверочное сообщение", 1, "Author",
                        isDelete = false, isRead = false
                    )
                )
            ), chatService.returnChats()[0]
        )
    }

    @Test(expected = ChatIsEmptyException::class)
    fun createIncorrectAddChatEmpty() {
        val chatService = ChatService
        chatService.addChat(Chat(5, 2, mutableListOf()))
    }

    @Test(expected = MessageToMyselfException::class)
    fun createIncorrectAddChatToMyself() {
        val chatService = ChatService
        chatService.addChat(Chat(5, 1, mutableListOf(Message(5, "Проверочное сообщение"))))
    }

    @Test
    fun correctReturnChats() {
        val chatService = ChatService
        chatService.addChat(Chat(5, 2, mutableListOf(Message(5, "Проверочное сообщение"))))
        chatService.addChat(Chat(7, 3, mutableListOf(Message(4, "Проверочное сообщение 2"))))
        assertEquals(
            mutableListOf(
                Chat(1, 2, mutableListOf(Message(1, "Проверочное сообщение"))),
                Chat(2, 3, mutableListOf(Message(1, "Проверочное сообщение 2")))
            ), chatService.returnChats()
        )
    }

    @Test
    fun correctReturnChatsAfterDelete() {
        val chatService = ChatService
        chatService.addChat(Chat(5, 2, mutableListOf(Message(5, "Проверочное сообщение"))))
        chatService.addChat(Chat(7, 3, mutableListOf(Message(4, "Проверочное сообщение 2"))))
        chatService.addChat(Chat(4, 4, mutableListOf(Message(7, "Проверочное сообщение 3"))))
        chatService.deleteChat(2)
        assertEquals(
            mutableListOf(
                Chat(1, 2, mutableListOf(Message(1, "Проверочное сообщение"))),
                Chat(3, 4, mutableListOf(Message(1, "Проверочное сообщение 3")))
            ), chatService.returnChats()
        )
    }

    @Test(expected = IdOutOfBoundsException::class)
    fun incorrectDeleteChat() {
        val chatService = ChatService
        chatService.addChat(Chat(5, 2, mutableListOf(Message(5, "Проверочное сообщение"))))
        chatService.deleteChat(2)
    }

    @Test
    fun createCorrectAddMessageEmptyChat() {
        val chatService = ChatService
        chatService.addMessage(1, Message(2, "Проверочное сообщение", 2))
        assertEquals(
            mutableListOf(
                Chat(1, 2, mutableListOf(Message(1, "Проверочное сообщение", 2)))
            ), chatService.returnChats()
        )
    }

    @Test
    fun createCorrectAddMessageCreateChat() {
        val chatService = ChatService
        chatService.addChat(Chat(5, 2, mutableListOf(Message(5, "Проверочное сообщение"))))
        chatService.addMessage(3, Message(2, "Проверочное сообщение 2"))
        assertEquals(
            mutableListOf(
                Chat(1, 2, mutableListOf(Message(1, "Проверочное сообщение"))),
                Chat(2, 3, mutableListOf(Message(1, "Проверочное сообщение 2", isRead = true)))
            ), chatService.returnChats()
        )
    }

    @Test
    fun createCorrectAddMessageUser() {
        val chatService = ChatService
        chatService.addChat(Chat(5, 2, mutableListOf(Message(5, "Проверочное сообщение"))))
        chatService.addMessage(2, Message(2, "Проверочное сообщение 2"))
        assertEquals(
            mutableListOf(
                Chat(
                    1, 2, mutableListOf(
                        Message(1, "Проверочное сообщение"),
                        Message(2, "Проверочное сообщение 2", isRead = true)
                    )
                )

            ), chatService.returnChats()
        )
    }

    @Test
    fun createCorrectAddMessageCompanion() {
        val chatService = ChatService
        chatService.addChat(Chat(5, 2, mutableListOf(Message(5, "Проверочное сообщение"))))
        chatService.addMessage(1, Message(2, "Проверочное сообщение 2", 2))
        assertEquals(
            mutableListOf(
                Chat(
                    1, 2, mutableListOf(
                        Message(1, "Проверочное сообщение"),
                        Message(2, "Проверочное сообщение 2", 2)
                    )
                )

            ), chatService.returnChats()
        )
    }

    @Test(expected = MessageToMyselfException::class)
    fun createIncorrectAddMessageToMyself() {
        val chatService = ChatService
        chatService.addMessage(2, Message(2, "Проверочное сообщение 2", 2))
    }

    @Test(expected = OutsiderChatException::class)
    fun createIncorrectAddOutsiderChat() {
        val chatService = ChatService
        chatService.addMessage(3, Message(2, "Проверочное сообщение 2", 2))
    }

    @Test(expected = ChatIsDeleteException::class)
    fun createIncorrectAddMessageUserIsDelete() {
        val chatService = ChatService
        chatService.addChat(Chat(5, 2, mutableListOf(Message(5, "Проверочное сообщение"))))
        chatService.addChat(Chat(7, 3, mutableListOf(Message(4, "Проверочное сообщение 2"))))
        chatService.deleteChat(2)
        chatService.addMessage(3, Message(2, "Проверочное сообщение 3"))
    }

    @Test(expected = ChatIsDeleteException::class)
    fun createIncorrectAddMessageCompanionIsDelete() {
        val chatService = ChatService
        chatService.addChat(Chat(5, 2, mutableListOf(Message(5, "Проверочное сообщение"))))
        chatService.addChat(Chat(7, 3, mutableListOf(Message(4, "Проверочное сообщение 2"))))
        chatService.deleteChat(2)
        chatService.addMessage(1, Message(2, "Проверочное сообщение 3", 3))
    }

    @Test
    fun correctDeleteMessage() {
        val chatService = ChatService
        chatService.addChat(Chat(5, 2, mutableListOf(Message(5, "Проверочное сообщение"))))
        chatService.addChat(Chat(7, 3, mutableListOf(Message(4, "Проверочное сообщение 2"))))
        chatService.deleteMessage(1, 1)
        assertEquals(
            mutableListOf(
                Chat(1, 2, mutableListOf(Message(1, "Проверочное сообщение", isDelete = true))),
                Chat(2, 3, mutableListOf(Message(1, "Проверочное сообщение 2")))
            ), chatService.returnChats()
        )
    }

    @Test(expected = IdOutOfBoundsException::class)
    fun incorrectDeleteMessage() {
        val chatService = ChatService
        chatService.addChat(Chat(5, 2, mutableListOf(Message(5, "Проверочное сообщение"))))
        chatService.addChat(Chat(7, 3, mutableListOf(Message(4, "Проверочное сообщение 2"))))
        chatService.deleteMessage(1, 2)
    }

    @Test
    fun correctEditMessage() {
        val chatService = ChatService
        chatService.addChat(Chat(5, 2, mutableListOf(Message(5, "Проверочное сообщение"))))
        chatService.editMessage(2, Message(1, "Исправленное сообщение"))
        assertEquals(
            mutableListOf(
                Chat(1, 2, mutableListOf(Message(1, "Исправленное сообщение", isRead = true)))
            ), chatService.returnChats()
        )
    }

    @Test(expected = AccessDeniedException::class)
    fun incorrectEditMessageAccessDenied() {
        val chatService = ChatService
        chatService.addChat(Chat(5, 2, mutableListOf(Message(5, "Проверочное сообщение"))))
        chatService.editMessage(1, Message(1, "Исправленное сообщение", 2))
    }

    @Test(expected = MessageToMyselfException::class)
    fun incorrectEditMessageToMyself() {
        val chatService = ChatService
        chatService.addChat(Chat(5, 2, mutableListOf(Message(5, "Проверочное сообщение"))))
        chatService.editMessage(1, Message(1, "Исправленное сообщение", 1))
    }

    @Test(expected = IncorrectParametersMessageException::class)
    fun incorrectEditMessageParameters() {
        val chatService = ChatService
        chatService.addChat(Chat(5, 2, mutableListOf(Message(5, "Проверочное сообщение"))))
        chatService.deleteMessage(1, 1)
        chatService.editMessage(2, Message(1, "Исправленное сообщение"))
    }

    @Test
    fun correctGetUnreadChatsCount() {
        val chatService = ChatService
        chatService.addChat(Chat(5, 2, mutableListOf(Message(5, "Проверочное сообщение"))))
        chatService.addChat(Chat(7, 3, mutableListOf(Message(4, "Проверочное сообщение 2"))))
        chatService.addChat(
            Chat(
                4, 4, mutableListOf(
                    Message(
                        7, "Проверочное сообщение 3",
                        isRead = true
                    )
                )
            )
        )
        assertEquals(2, chatService.getUnreadChatsCount())
    }

    @Test
    fun correctGetMessageByChatId() {
        val chatService = ChatService
        chatService.addChat(Chat(5, 2, mutableListOf(Message(5, "Проверочное сообщение"))))
        chatService.addMessage(2, Message(2, "Проверочное сообщение 2"))
        chatService.addMessage(2, Message(3, "Проверочное сообщение 3"))
        assertEquals(
            "Проверочное сообщение \nПроверочное сообщение 2 \nПроверочное сообщение 3 \n",
            chatService.getMessageByChatId(1)
        )
    }

    @Test
    fun correctGetMessageByChatIdIsEmpty() {
        val chatService = ChatService
        chatService.addChat(Chat(5, 2, mutableListOf(Message(5, "Проверочное сообщение"))))
        chatService.deleteMessage(1, 1)
        assertEquals("нет сообщений", chatService.getMessageByChatId(1))
    }

    @Test(expected = IdOutOfBoundsException::class)
    fun incorrectGetMessageByChatId() {
        val chatService = ChatService
        chatService.addChat(Chat(5, 2, mutableListOf(Message(5, "Проверочное сообщение"))))
        chatService.getMessageByChatId(2)
    }

    @Test
    fun correctGetMessageByCountAndAuthorId() {
        val chatService = ChatService
        chatService.addChat(Chat(5, 2, mutableListOf(Message(5, "Проверочное сообщение"))))
        chatService.addMessage(2, Message(2, "Проверочное сообщение 2"))
        chatService.addMessage(2, Message(3, "Проверочное сообщение 3"))
        assertEquals(
            mutableListOf(
                Message(3, "Проверочное сообщение 3", isRead = true),
                Message(2, "Проверочное сообщение 2", isRead = true),
                Message(1, "Проверочное сообщение", isRead = true)
            ), chatService.getMessageByCountAndAuthorId(3, 2)
        )
    }

    @Test(expected = AuthorIdIsAbsentException::class)
    fun incorrectGetMessageByCountAndAuthorId() {
        val chatService = ChatService
        chatService.addChat(Chat(5, 2, mutableListOf(Message(5, "Проверочное сообщение"))))
        chatService.getMessageByCountAndAuthorId(1, 3)
    }
}