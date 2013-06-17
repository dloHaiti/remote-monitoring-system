dataSource {
    pooled = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}
// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = "jdbc:h2:dloDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
        }
    }
    production {
      dataSource {
        dbCreate = "update"  // Uncomment to modify tables
        url = "jdbc:mysql://localhost:3306/dlo"
        driverClassName = "com.mysql.jdbc.Driver"
        username = "app"
        password = "SQ7cuYGAMVtVZh+J"
        properties {
          maxActive = 100
          maxIdle = 25
          minIdle = 5
          initialSize = 10
          minEvictableIdleTimeMillis = 60000
          timeBetweenEvictionRunsMillis = 60000
          maxWait = 10000
        }
      }
    }
}
