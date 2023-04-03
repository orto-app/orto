package coop.uwutech.orto.shared.domain.model.core

sealed class Resource<out T> {
    class Success<T>(val data: T) : Resource<T>() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Success<*>

            if (data != other.data) return false

            return true
        }

        override fun hashCode(): Int {
            return data?.hashCode() ?: 0
        }

        override fun toString(): String {
            return "Success(data=$data)"
        }
    }

    class Error(val exception: Exception) : Resource<Nothing>() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Error

            if (exception != other.exception) return false

            return true
        }

        override fun hashCode(): Int {
            return exception.hashCode()
        }
    }
}