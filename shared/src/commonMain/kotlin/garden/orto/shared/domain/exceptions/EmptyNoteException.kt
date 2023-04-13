package garden.orto.shared.domain.exceptions

class EmptyNoteException(message: String?, cause: Throwable?) : Exception(message, cause) {
    constructor(message: String?) : this(message, null)
}