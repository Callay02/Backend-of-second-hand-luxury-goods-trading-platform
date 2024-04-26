package icu.callay.util;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * &#064;projectName:    springboot
 * &#064;package:        icu.callay.util
 * &#064;className:      StartupTimeListener
 * &#064;author:     Callay
 * &#064;description:  记录开机时间
 * &#064;date:    2024/4/26 23:10
 * &#064;version:    1.0
 */
@Component
public class StartupTimeListener implements ApplicationListener<ApplicationStartedEvent> {

    private Long startupTime;
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        startupTime =System.currentTimeMillis();
    }

    public long getStartupTime(){
        return startupTime;
    }
}
