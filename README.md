common-resteasy
===============

Utility extensions for [RESTEasy](http://resteasy.jboss.org/). Includes logging and exception handling.

It logs full details for the REST calls which look like

    b.w.c.r.l.PreProcessLoggingInterceptor - Service: GET http://localhost:8123/common-resteasy/rest/sample
    HTTP request headers:
        Cookie: __utma=111872281.549164452.1375780757.1389824220.1397418693.4; __utmz=111872281.1375780757.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __test=1; _ga=GA1.1.549164452.1375780757
        Connection: keep-alive
        Accept-Language: en-US,en;q=0.5
        Host: localhost:8123
        Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
        User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:32.0) Gecko/20100101 Firefox/32.0
        Accept-Encoding: gzip, deflate
        Referer: http://localhost:8123/common-resteasy/
    Response types text/plain
    Cookies: 
        __test -> __test=1
        __utmz -> __utmz=111872281.1375780757.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)
        _ga -> _ga=GA1.1.549164452.1375780757
        __utma -> __utma=111872281.549164452.1375780757.1389824220.1397418693.4
    Query Parameters: 
        q -> [tset]
    Path parameters: 
    Reply type: class java.lang.String
    Output document:
    done at 14:45
    Duur: 2.936s
    
There is integration for gathering profiling data. For the sample this results in profiling info like:
    
    ++++ REST calls:
                                                                 |          6 |      12431 |    2071.83
                                                           group |      count | total time |   avg time
                                        ProfilingService:profile |          1 |          8 |       8.00
                                             SampleService:other |          1 |       2999 |    2999.00
                                            SampleService:sample |          4 |       9424 |    2356.00
    
    
    ++++ JDBC calls (by method):
                                                                 |          0 |          0 |       0.00
                                                           group |      count | total time |   avg time
    
    
    ++++ JDBC calls (by REST service):
                                                                 |          0 |          0 |       0.00
                                                           group |      count | total time |   avg time
    
    
    ++++ gateway calls (by method):
                                                                 |          5 |       7145 |    1429.00
                                                           group |      count | total time |   avg time
                                                         gateway |          4 |       6084 |    1521.00
                                                        starship |          1 |       1061 |    1061.00
    
    
    ++++ gateway calls (by REST service):
                                                                 |          5 |       7145 |    1429.00
                                                           group |      count | total time |   avg time
                                             SampleService:other |          1 |       1061 |    1061.00
                                            SampleService:sample |          4 |       6084 |    1521.00
    
    
    ++++ data grouped by REST call:
                                                           group |      count | total time |   avg time | %total
                                            SampleService:sample |          4 |       9424 |    2356.00 | 100.00
                                                            JDBC |          0 |          0 |       0.00 |   0.00
                                                         gateway |          4 |       6084 |    2356.00 |  64.56
    
                                                               * |          6 |      12431 |    2071.83 | 100.00
                                                            JDBC |          0 |          0 |       0.00 |   0.00
                                                         gateway |          5 |       7145 |    2486.20 |  57.48
    
                                             SampleService:other |          1 |       2999 |    2999.00 | 100.00
                                                            JDBC |          0 |          0 |       0.00 |   0.00
                                                         gateway |          1 |       1061 |    2999.00 |  35.38
    
                                        ProfilingService:profile |          1 |          8 |       8.00 | 100.00
                                                            JDBC |          0 |          0 |       0.00 |   0.00
                                                         gateway |          0 |          0 |       0.00 |   0.00


Have a look at the web project for a sample of use with profiling (based on [ew-profiling](https://github.com/joachimvda/ew-profiling)).
After compilations, you can use "mvn cargo:run" to run the demo application and surf to [http://localhost:8123/common-resteasy/]. 
