package exceptions

class IdOutOfBoundsException
    (message: String = "Указан неверный Id") : RuntimeException(message)

class ChatIsEmptyException
    (message: String = "В чате должно быть не менее одно сообщения") : RuntimeException(message)

class MessageToMyselfException
    (message: String = "Нельзя писать сообщения самому себе") : RuntimeException(message)

class OutsiderChatException
    (message: String = "Вы не можете участвовать в посторонних чатах") : RuntimeException(message)

class ChatIsDeleteException
    (message: String = "Вы не можете добавлять сообщения в удалённый чат") : RuntimeException(message)

class AccessDeniedException
    (message: String = "Вы не можете редактировать чужие сообщения") : RuntimeException(message)

class IncorrectParametersMessageException
    (message: String = "Неверные параметры сообщения") : RuntimeException(message)

class AuthorIdIsAbsentException
    (message: String = "Чат с указанным собеседником отсутствует") : RuntimeException(message)
