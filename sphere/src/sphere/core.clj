(ns sphere.core
  (:use [penumbra opengl]
        [cantor]
        [clojure.contrib.def])
  (:require [penumbra [app :as app] [text :as text]]))

(defn sphere-vertices
  [lod]
  (for [theta (range 0 361 (/ 360 lod))]
    (for [phi (range -90 91 (/ 180 (/ lod 2)))]
      (cartesian (polar3 theta phi)))))

(defn init [state]
  (app/title! "Sphere")
  (enable :normalize)
  (enable :depth-test)
;  (enable :cull-face)
  (enable :lighting)
  (enable :light0)
  (enable :fog)
  (shade-model :flat)
  state)

(defn reshape [[x y width height] state]
  (frustum-view 50 (/ (double width) height) 0.1 100)
  (load-identity)
  (translate 0 -0.35 -5.75)
  (light 0
    :position [-1 -1 -1 0])
  (fog
    :fog-mode :exp
    :fog-density 0.15
    :fog-start 0
    :fog-end 10
    :fog-color [0 0 0 0])
  state)

(defn mouse-drag [[dx dy] _ button state]
  (assoc state
    :rot-x (+ (:rot-x state) dy)
    :rot-y (+ (:rot-y state) dx)))

(defn draw-square [w]
    (draw-quads
      (normal 0 0 -1)
      (vertex w  0 1)
      (vertex 0  w 1)
      (vertex (- 0 w) 0 1)
      (vertex 0 (- 0 w) 1)
))

(defn draw-squares [n]
  (material :front-and-back
    :ambient-and-diffuse [0 1 0 1])
    (dotimes [_ n]
      (rotate (/ 360 n) 0 1 0)
      (draw-square 0.1)))

(defn render-sphere [n]
  (dotimes [_ n]
    (rotate (/ 360 n) 1 0 0)
    (draw-squares n)))

(defn display [[delta time] state]
  (rotate (:rot-x state) 1 0 0)
  (rotate (:rot-y state) 0 1 0)
  (clear-color 0 0 0 1)
  (clear)
  (render-sphere 30)
  (app/repaint!))

(defn start []
  (app/start
    {:init init
     :mouse-drag mouse-drag
     :display display
     :reshape reshape } {:rot-x 0, :rot-y 0}))

