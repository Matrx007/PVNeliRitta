PWD = $(shell pwd)

JC = javac

LIBRARIES = $(PWD)/lib/MXEngine.jar

CP = $(LIBRARIES)
SP = "$(PWD)/src/"
OP = "$(PWD)/build"

JFLAGS = -g -verbose

all: build/com/pv/neliritta/gui/states/InGame.class build/com/pv/neliritta/gui/states/InGamePauseMenu.class build/com/pv/neliritta/gui/states/State.class build/com/pv/neliritta/gui/states/LoadGame.class build/com/pv/neliritta/gui/states/MainMenu.class build/com/pv/neliritta/gui/states/MatchOptions.class build/com/pv/neliritta/gui/states/SaveGame.class build/com/pv/neliritta/gui/Action.class build/com/pv/neliritta/gui/ingame/Board.class build/com/pv/neliritta/gui/Component.class build/com/pv/neliritta/gui/components/Button.class build/com/pv/neliritta/gui/components/Options.class build/com/pv/neliritta/gui/components/ScrollList.class build/com/pv/neliritta/gui/components/Input.class build/com/pv/neliritta/gui/components/Label.class build/com/pv/neliritta/gui/Color.class build/com/pv/neliritta/AbstractSpaceBackground.class build/com/pv/neliritta/constraints/Constraint.class build/com/pv/neliritta/backend/BackEnd.class build/com/pv/neliritta/backend/SaveData.class build/com/pv/neliritta/backend/SaveManager.class build/com/pv/neliritta/FontManager.class build/com/pv/neliritta/Main.class build/com/pv/neliritta/Utilities.class build/com/pv/neliritta/GUI.class build/com/pv/neliritta/localization/Localization.class build/com/pv/neliritta/GraphicsManager.class 
	unzip -d build/ $(shell printf "$(LIBRARIES)" | tr ':' ' ')
	cp -r src/META-INF build/
	jar cvmf build/META-INF/MANIFEST.MF NeliRitta.jar -C build/ .

build/com/pv/neliritta/gui/states/InGame.class: src/com/pv/neliritta/gui/states/InGame.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/gui/states/InGame.java
build/com/pv/neliritta/gui/states/InGamePauseMenu.class: src/com/pv/neliritta/gui/states/InGamePauseMenu.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/gui/states/InGamePauseMenu.java
build/com/pv/neliritta/gui/states/State.class: src/com/pv/neliritta/gui/states/State.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/gui/states/State.java
build/com/pv/neliritta/gui/states/LoadGame.class: src/com/pv/neliritta/gui/states/LoadGame.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/gui/states/LoadGame.java
build/com/pv/neliritta/gui/states/MainMenu.class: src/com/pv/neliritta/gui/states/MainMenu.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/gui/states/MainMenu.java
build/com/pv/neliritta/gui/states/MatchOptions.class: src/com/pv/neliritta/gui/states/MatchOptions.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/gui/states/MatchOptions.java
build/com/pv/neliritta/gui/states/SaveGame.class: src/com/pv/neliritta/gui/states/SaveGame.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/gui/states/SaveGame.java
build/com/pv/neliritta/gui/Action.class: src/com/pv/neliritta/gui/Action.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/gui/Action.java
build/com/pv/neliritta/gui/ingame/Board.class: src/com/pv/neliritta/gui/ingame/Board.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/gui/ingame/Board.java
build/com/pv/neliritta/gui/Component.class: src/com/pv/neliritta/gui/Component.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/gui/Component.java
build/com/pv/neliritta/gui/components/Button.class: src/com/pv/neliritta/gui/components/Button.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/gui/components/Button.java
build/com/pv/neliritta/gui/components/Options.class: src/com/pv/neliritta/gui/components/Options.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/gui/components/Options.java
build/com/pv/neliritta/gui/components/ScrollList.class: src/com/pv/neliritta/gui/components/ScrollList.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/gui/components/ScrollList.java
build/com/pv/neliritta/gui/components/Input.class: src/com/pv/neliritta/gui/components/Input.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/gui/components/Input.java
build/com/pv/neliritta/gui/components/Label.class: src/com/pv/neliritta/gui/components/Label.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/gui/components/Label.java
build/com/pv/neliritta/gui/Color.class: src/com/pv/neliritta/gui/Color.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/gui/Color.java
build/com/pv/neliritta/AbstractSpaceBackground.class: src/com/pv/neliritta/AbstractSpaceBackground.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/AbstractSpaceBackground.java
build/com/pv/neliritta/constraints/Constraint.class: src/com/pv/neliritta/constraints/Constraint.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/constraints/Constraint.java
build/com/pv/neliritta/backend/BackEnd.class: src/com/pv/neliritta/backend/BackEnd.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/backend/BackEnd.java
build/com/pv/neliritta/backend/SaveData.class: src/com/pv/neliritta/backend/SaveData.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/backend/SaveData.java
build/com/pv/neliritta/backend/SaveManager.class: src/com/pv/neliritta/backend/SaveManager.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/backend/SaveManager.java
build/com/pv/neliritta/FontManager.class: src/com/pv/neliritta/FontManager.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/FontManager.java
build/com/pv/neliritta/Main.class: src/com/pv/neliritta/Main.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/Main.java
build/com/pv/neliritta/Utilities.class: src/com/pv/neliritta/Utilities.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/Utilities.java
build/com/pv/neliritta/GUI.class: src/com/pv/neliritta/GUI.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/GUI.java
build/com/pv/neliritta/localization/Localization.class: src/com/pv/neliritta/localization/Localization.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/localization/Localization.java
build/com/pv/neliritta/GraphicsManager.class: src/com/pv/neliritta/GraphicsManager.java
	mkdir -p build/
	$(JC) $(JFLAGS) -d $(OP) -sourcepath $(SP) -classpath $(CP) src/com/pv/neliritta/GraphicsManager.java

clean:
	rm -rf build/
	rm NeliRitta.jar