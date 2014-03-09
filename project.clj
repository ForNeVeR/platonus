(defproject platonus/platonus "0.1.21"
  :repositories [["releases" {:url "http://fornever.me:18080/repository/codingteam/"
                              :sign-releases false}]
                 ["snapshots" {:url "http://fornever.me:18080/repository/codingteam-snapshots/"
                               :sign-releases false}]]
  :description "Simple Markov network library."
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [com.googlecode.soundlibs/mp3spi "1.9.5-1"]
                 [seesaw "1.4.2"]]
  :aot [platonus.network-java platonus.filesystem]
  :main platonus.core)
