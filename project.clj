(defproject rentpath "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/core.async "1.3.618"]
                 [cprop "0.1.17"]
                 [clj-http "3.12.1"]
                 [mount "0.1.16"]
                 [cheshire "5.10.0"]
                 [http-kit "2.3.0"]
                 [compojure "1.6.2"]
                 [ring/ring-core "1.8.2"]
                 [ring/ring-json "0.5.1"]
                 [ring/ring-defaults "0.3.2"]]
  :main ^:skip-aot rentpath.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true "]}
             :dev {:jvm-opts ["-Dconf=resources/config.edn"]}})
