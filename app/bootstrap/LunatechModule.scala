package bootstrap

import com.google.inject.AbstractModule

class LunatechModule extends AbstractModule {
  protected def configure: Unit = {
    //bind(classOf[InitialData]).asEagerSingleton()
  }
}
