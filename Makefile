build: src/main/scala/*
	sbt "runMain muxgen.NamedMuxGenApp --target-dir=./build"

test: src/main/scala/* src/test/scala/*
	sbt "testOnly muxgen.*Tester"
