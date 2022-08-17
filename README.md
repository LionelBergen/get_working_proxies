ger_working_proxies
-------------------
Automatically get lists of latest proxies from various free proxy websites, then in an automated browser (selenium), ensure these proxies work and hide users IP.
Webdriver / Selenium takes care of opening a browser with a proxy, while an external static website is used to ensure the IP address is hidden and internet connection works.

Requirements
------------
**JAVA 8+** (tested with java 18, needed for gradle version)  
**MY_IP** - *Optional* Environment Variable containing your IP address, so that the proxies will also test that your IP is hidden and fail otherwise.