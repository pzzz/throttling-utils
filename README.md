# throttling-utils
![Jenkins Build Status](http://cdn.pzzz.de/throttling-utils-status.svg)
-----
Framework and library to do both:
- handle throttling at the client side
- implement throttling at a server side
## Usage
Can be used as a servlet filter via web.xml
```xml
<filter>
  <filter-name>spikeArrest</filter-name>
  <filter-class>de.pzzz.throttling.utils.servlet.IpThrottlingServletFilter</filter-class>
  <init-param>
    <param-name>limit</param-name>
    <param-value>3</param-value>
  </init-param>
	<init-param>
		<param-name>duration</param-name>
		<param-value>10</param-value>
	</init-param>
</filter>
<filter-mapping>
	<filter-name>spikeArrest</filter-name>
	<url-pattern>/*</url-pattern>
</filter-mapping>
```
or directly in the code from a ServletContextListener
```java
@Override
public void contextInitialized(ServletContextEvent event) {
	ServletContext context = event.getServletContext();
	context.addFilter("spikeArrest", new IpThrottlingServletFilter());
}
```
