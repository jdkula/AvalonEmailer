open class Role(val name: String) {
    override fun toString(): String {
        return name
    }
}

class Good(name: String) : Role(name)
class Evil(name: String) : Role(name)