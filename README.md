# winton-utils

A Clojure utility library for:

* Safe number parsing

### Installation (Leiningen)

Install from git using JitPack
[![Release](https://jitpack.io/v/WintonCentre/winton-utils.svg)]
(https://jitpack.io/#WintonCentre/winton-utils)

Add at the end of repositories in `project.clj`
```clj
:repositories [["jitpack" "https://jitpack.io"]]
```

Add the dependency, for example:
```clj
:dependencies [[com.github.WintonCentre/winton-utils "winton-utils-v0.0.1"]]
```

## Usage

```clj
(ns your-ns
    (:require [number/conversions :refer [parse-number parse-int])
)
```

## License

Copyright © 2017 Mike Pearson, University of Cambridge

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
