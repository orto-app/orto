package garden.orto.shared.base.map

abstract class Mapper<M, P> {
    abstract fun map(model: M): P
    abstract fun inverseMap(model: P): M

    fun map(values: Iterable<M>): List<P> = values.map { map(it) }
    fun inverseMap(values: Iterable<P>): List<M> = values.map { inverseMap(it) }
}