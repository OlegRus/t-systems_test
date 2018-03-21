# t-systems_test

тест можно запустить в chrome и в firefox.

- для запуска в firefox необходима версия Firefox ESR 52.7.2.
- для запуска в chrome необходимо скачать chromedriver и в browsers.json указать путь к нему.

По умолчанию тест запускается в firefox, для запуска в нескольких браузерах используется testng.xml

```xml
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="kitten">
    <test name="chrome">
        <parameter name="browser" value="chrome"/>
        <classes>
            <class name="HelloTest"/>
        </classes>
    </test>
    <test name="firefox">
        <parameter name="browser" value="firefox"/>
        <classes>
            <class name="HelloTest"/>
        </classes>
    </test>
</suite>
```
