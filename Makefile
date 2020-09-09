build: src/main/scala/*
	sbt "runMain muxgen.MuxGenApp --target-dir=./build"

test: src/main/scala/* src/test/scala/*
	sbt "testOnly muxgen.MuxgenTester"
