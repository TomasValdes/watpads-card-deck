{
  description = "A Spring Boot Gradle project using Kotlin";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
      in
      rec {
        devShell = pkgs.mkShell {
          buildInputs = [
            pkgs.openjdk17
            pkgs.gradle
            # pkgs.git
          ];

          shellHook = ''
            export JAVA_HOME=${pkgs.openjdk}
            export PATH=$JAVA_HOME/bin:$PATH
          '';
        };

        packages.default = pkgs.stdenv.mkDerivation {
          name = "spring-boot-gradle-kotlin";

          src = ./.;

          buildInputs = [
            pkgs.openjdk
            pkgs.gradle
          ];

          buildPhase = ''
            ./gradlew build
          '';

          installPhase = ''
            mkdir -p $out/lib
            cp -r build/libs/* $out/lib/
          '';
        };

        defaultApp = flake-utils.lib.mkApp {
          drv = self.packages.${system}.default;
          program = "${self.packages.${system}.default}/bin/Watpads-card-deck";
        };
      });
}

