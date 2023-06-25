package guru.qa.niffler.page;
import guru.qa.niffler.config.Config;

public abstract class BasePage<T extends BasePage> {

    public abstract T checkThatPageLoaded();
}
