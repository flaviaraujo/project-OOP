.PHONY: build diagram report install clean

build:
	javac -Xlint:unchecked \
		src/users/*.java \
		src/activities/*.java \
		src/exceptions/*.java \
		src/*.java

run:
	java src.Controller

diagram:
	plantuml -tpng diagram/diagram.puml && \
	plantuml -tpng diagram/exceptions.puml

report:
	cd report && \
	makeindex output/main.nlo -s output/nomencl.ist -o output/main.nls && \
	xelatex -output-directory=output -shell-restricted main.tex

install:
	# @echo "Installing prettier for code formatting"
	# npm install -g prettier
	# npm install prettier-plugin-java --save-dev
	@echo "Installing plantuml for diagram generation"
	sudo apt install -y plantuml
	@echo "Installing openjdk-21-jdk for java development"
	sudo apt install -y openjdk-21-jdk
	@echo "Installing latex for report generation"
	sudo apt install -y texlive-xetex texlive-lang-portuguese texlive-latex-recommended ttf-mscorefonts-installer

clean:
	rm -rf \
		src/users/*.class \
		src/activities/*.class \
		src/exceptions/*.class \
		src/*.class
