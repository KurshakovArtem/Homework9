package service

import data.*
import exceptions.*

object ChatService {
    private var chats: MutableList<Chat> = mutableListOf()

    // для поиска Id автора во всех сообщениях всех чатов
    private val compareAuthorId = fun(id: Int): Boolean {
        return chats.any { chat -> chat.messages.any { id == it.authorId } }
    }

    fun addChat(chat: Chat): Int {
        if (chat.messages.isEmpty()) {            // не допускаются чаты без сообщений (пустые)
            throw ChatIsEmptyException()
        }
        // проверка на сообщение самому себе
        if (chat.companionId == 1) {
            throw MessageToMyselfException()
        }
        chats += chat.copy(id = chats.lastIndex + 2)
        chats.last().messages.forEachIndexed { index, message -> message.id = index + 1 }
        return chats.last().id

    }


    fun returnChats(): List<Chat> {
        return chats.filter { !it.isDelete }
    }

    fun deleteChat(id: Int): Boolean {
        if (chats.any { it.id == id && !it.isDelete }) {
            chats[id - 1].isDelete = true
            return true
        }
        throw IdOutOfBoundsException()
    }

    fun addMessage(companionId: Int, message: Message): Boolean {
        // проверяем на сообщение самому себе
        if (message.authorId == companionId) {
            throw MessageToMyselfException()
        }
        // Пользователь не может участвовать в посторонних чатах
        if (message.authorId != 1 && companionId != 1) {
            throw OutsiderChatException()
        }
        // если сообщение от пользователя
        if (message.authorId == 1) {
            // проверяем есть ли уже созданный чат с companionId
            if (compareAuthorId(companionId) || chats.any { it.companionId == companionId }) {
                chats.forEachIndexed { index, chat ->
                    if ((chat.messages.any { companionId == it.authorId }
                                || chat.companionId == companionId )
                        && !chats[index].isDelete
                    ) {
                        chats[index].messages += message.copy(id = chats[index].messages.lastIndex + 2, isRead = true)
                        return true
                    }
                }
                throw ChatIsDeleteException()
            }
            // если нет, создаем чат
            else {
                addChat(
                    Chat(
                        1, companionId = companionId,
                        messages = mutableListOf(message.copy(id = 1, isRead = true))
                    )
                )
                return true
            }
        }
        // проверяем есть ли уже созданный чат с данным автором сообщения
        if (compareAuthorId(message.authorId) || chats.any { it.companionId == message.authorId }) {
            chats.forEachIndexed { index, chat ->
                if ((chat.messages.any { message.authorId == it.authorId }
                            || chat.companionId == message.authorId)
                    && !chats[index].isDelete
                ) {
                    chats[index].messages += message.copy(id = chats[index].messages.lastIndex + 2)
                    return true
                }
            }
            throw ChatIsDeleteException()
        }
        // если нет, создаем чат
        else {
            addChat(Chat(1, companionId = message.authorId, messages = mutableListOf(message.copy(id = 1))))
        }
        return true
    }

    fun deleteMessage(chatId: Int, messageId: Int): Boolean {
        if (chats.any { chatId == it.id } && chats[chatId - 1].messages.any { messageId == it.id }) {
            chats[chatId - 1].messages[messageId - 1].isDelete = true
            return true
        }
        throw IdOutOfBoundsException()
    }

    fun editMessage(companionId: Int, message: Message): Boolean {
        // можно редактировать только свои сообщения
        if (message.authorId != 1) {
            throw AccessDeniedException()
        }
        // проверяем на сообщение самому себе
        if (message.authorId == companionId) {
            throw MessageToMyselfException()
        }
        // проверяем существует ли чат с companionId
        if (compareAuthorId(companionId) || chats.any { it.companionId == companionId }) {
            chats.forEachIndexed { index, chat ->
                if ((chat.messages.any { companionId == it.authorId } || chat.companionId == companionId)
                    && !chats[index].isDelete
                    && chats[index].messages.any { it.id == message.id }
                    && !chats[index].messages[message.id - 1].isDelete) {
                    chats[index].messages[message.id - 1] = message.copy(isRead = true)
                    return true
                }
            }
        }
        throw IncorrectParametersMessageException()
    }

    fun getUnreadChatsCount(): Int {
        var count = 0
        chats.forEach { chat -> if (chat.messages.any { !it.isRead }) count++ }
        return count
    }

    fun getMessageByChatId(chatId: Int): String {
        if (chatId !in 1..chats.size) {
            throw IdOutOfBoundsException()
        }
        var strCount = ""
        chats[chatId - 1].messages.forEach { if (!it.isDelete) strCount += it.message + " \n" }
        if (strCount == "") {
            return "нет сообщений"
        }
        return strCount
    }

    // возвращает список последних сообщений (количество сообщений count) по ID автора
    fun getMessageByCountAndAuthorId(count: Int, authorId: Int): MutableList<Message> {
        if (!chats.any { it.companionId == authorId }) {
            throw AuthorIdIsAbsentException()
        }
        val chatIndex = chats.indexOfFirst { it.companionId == authorId }
        val messageList: MutableList<Message> = mutableListOf()
        chats[chatIndex].messages.reversed().forEachIndexed { index, message ->
            if (messageList.size <= count) messageList += message
            chats[chatIndex].messages[chats[chatIndex].messages.size - index - 1].isRead = true
        }
        return messageList
    }

    fun clear() {
        chats = mutableListOf()
    }
}