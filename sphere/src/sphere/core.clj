(ns sphere.core
  (:use [penumbra opengl]
        [cantor]
        [clojure.contrib.def])
  (:require [penumbra [app :as app] [text :as text]]))

(defn reshape [[x y width height] state]
  (frustum-view 50 (/ (double width) height) 0.1 100)
  (load-identity)
  (translate 0 -0.35 -5.75)
  (light 0
    :position [1 -1 -1 0])
  (fog
    :fog-mode :exp
    :fog-density 0.05
    :fog-start 0
    :fog-end 10
    :fog-color [0 0 0 0])
  state)

(defn mouse-drag [[dx dy] _ button state]
  (assoc state
    :rot-x (+ (:rot-x state) dy)
    :rot-y (+ (:rot-y state) dx)))

(defn sphere-vertices
  [lod]
  (for [theta (range 0 361 (/ 360 lod))]
    (for [phi (range -90 91 (/ 180 (/ lod 2)))]
      (cartesian (polar3 theta phi)))))

(defn draw-sphere [vertices]
  (material :front-and-back
    :ambient-and-diffuse [0.7 0.7 1 1]
    :specular            [1 1 1 0.5]
    :shininess           1000)
  (doseq [arcs (partition 2 1 vertices)]
    (draw-quad-strip
     (doseq [[[a b] [c d]] (partition 2 1 (map list (first arcs) (second arcs)))]
       (let [u (sub a d)
             v (sub b d)]
         (normal (normalize (cross u v))))
       (vertex a)
       (vertex b)))))

(defn render-sphere [n]
  (draw-sphere (sphere-vertices n)))

(defn init-sphere []
  (def sphere (create-display-list (render-sphere 100))))

(defn display [[delta time] state]
  (rotate (:rot-x state) 1 0 0)
  (rotate (:rot-y state) 0 1 0)
  (clear-color 0 0 0 1)
  (clear)
  (call-display-list  sphere)
  (app/repaint!))

(defn init [state]
  (init-sphere)
  (app/title! "Sphere")
  (enable :normalize)
  (enable :depth-test)
;  (enable :cull-face)
  (enable :lighting)
  (enable :light0)
  (enable :fog)
  (shade-model :smooth)
  state)

(defn start []
  (app/start
    {:init init
     :mouse-drag mouse-drag
     :display display
     :reshape reshape } {:rot-x 0, :rot-y 0}))

