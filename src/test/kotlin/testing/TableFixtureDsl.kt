package testing

fun temp() {
    setupTable {
        insertTenant() associate {
            insertUser() associate {
                insertTask()
            }
        }
    }
}

fun setupTable(block: RootContext.() -> Unit) {
    RootContext().block()
}

@DslMarker
private annotation class TableFixtureDsl

interface TableFixture

class TenantTableFixture : TableFixture

fun RootContext.insertTenant() = insert(TenantTableFixture())

class UserTableFixture : TableFixture

fun AssociateContext<TenantTableFixture>.insertUser() = insert(UserTableFixture())

class TaskTableFixture : TableFixture

fun AssociateContext<UserTableFixture>.insertTask() = insert(TaskTableFixture())

@TableFixtureDsl
interface IfFixtureContext<T : TableFixture> {
    private val thisContext get() = this

    fun <T : TableFixture> insert(fixture: T): T

    infix fun <T : TableFixture> TableFixture.associate(block: AssociateContext<T>.() -> Unit) {
        AssociateContext<T>(thisContext).block()
    }
}

class DummyRootFixture : TableFixture

class RootContext : IfFixtureContext<DummyRootFixture> {
    private val mutableFixtures = mutableListOf<TableFixture>()

    override fun <T : TableFixture> insert(fixture: T): T {
        mutableFixtures.add(fixture)
        return fixture
    }
}

class AssociateContext<T : TableFixture>(
    private val parentContext: IfFixtureContext<*>
) : IfFixtureContext<T> {
    override fun <T : TableFixture> insert(fixture: T): T {
        parentContext.insert(fixture)
        return fixture
    }
}
