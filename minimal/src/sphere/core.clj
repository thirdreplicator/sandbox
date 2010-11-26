(ns minimal.core
  (:use [penumbra opengl]
        [clojure.contrib.def])
  (:require [penumbra [app :as app] [text :as text]]))

(defn init [state]
  (app/title! ""))

(defn display [[delta _] state]
  (color 0 1 0)
  (clear-color 1 1 1 1)
  (clear)
  (app/repaint!))

(defn start []
  (app/start
    {:init init
     :display display } {}))

