.PHONY: build lint doc diagram report install clean

build: lint
	javac src/*.java

run:
	java -cp src Main

lint:
	prettier --write "src/*.java"

doc:
	javadoc -d doc src/*.java

diagram:
	plantuml -tpng diagram/diagram.puml

report:
	cd report && \
	makeindex output/main.nlo -s output/nomencl.ist -o output/main.nls && \
	xelatex -output-directory=output -shell-restricted main.tex

install:
	@echo "Installing prettier for code formatting"
	npm install prettier-plugin-java --save-dev
	@echo "Installing plantuml for diagram generation"
	sudo apt install -y plantuml
	@echo "Installing openjdk-17-jdk for java development"
	sudo apt install -y openjdk-17-jdk
	@echo "Installing latex for report generation"
	sudo apt install -y texlive-xetex texlive-lang-portuguese texlive-latex-recommended ttf-mscorefonts-installer

clean:
	rm -rf src/*.class

