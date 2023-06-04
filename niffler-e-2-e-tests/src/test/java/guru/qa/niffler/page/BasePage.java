package guru.qa.niffler.page;

import guru.qa.niffler.config.Config;

public abstract class BasePage<T extends BasePage<T>> {
    public static final String BASE_URL = Config.getConfig().getBaseUrl();



    public abstract T checkThatPageLoaded();


}
