# Pattern Recognition of Stomatal Distribution using Life-Like Network Automata (LLNA)

This repository contains the implementation of a graph-based pattern recognition framework to classify stomatal spatial distribution under environmental stress. The method models stomatal centroids as networks and extracts dynamical signatures using Life-Like Network Automata (LLNA).

## Overview

Plants exhibit plastic stomatal distributions in response to environmental changes. This project proposes a computational framework that:

- Constructs networks from stomatal centroid coordinates
- Applies Life-Like Network Automata (LLNA) to simulate spatio-temporal dynamics
- Extracts feature vectors from automata evolution
- Classifies environmental stress conditions using machine learning (SVM)

The approach captures topological and dynamical properties beyond traditional stomatal counting or segmentation.

## Repository Structure

Main Java source code:

```

stomata-LLNA/NetworkAutomata/src/CA/graph/mainEstomatos

```

Key Java files:

- `GenerateNetworksStomata.java` — Network construction from stomatal centroids
- `TEP_SpatioTimeFromStomataNetworks.java` — Spatio-temporal evolution extraction (TEP)
- `SVM_LLNA_estomatos.java` — Classification using Support Vector Machine
- `SelectRuleStomata_TrandescantiaLight.java` — Rule selection for Tradescantia
- `SelectRuleStomata_CalisiaZebrina.java` — Rule selection for Callisia
- `SelectRuleStomata_CthenanteLight.java` — Rule selection for Ctenanthe
- `FeatExtractStomata_TrandescantiaLight.java` — Feature extraction (Tradescantia)
- `FeatExtractStomata_Callisia.java` — Feature extraction (Callisia)
- `FeatExtractStomata_CallisiaV2.java` — Alternative feature extraction (Callisia)
- `FeatExtracStomata_CethenanteLight.java` — Feature extraction (Ctenanthe)

Additional scripts:

- Python — plotting and analysis
- MATLAB — visualization and auxiliary plots

## Method Pipeline

1. Extract stomatal centroid coordinates
2. Construct spatial networks
3. Apply LLNA to simulate network dynamics
4. Extract dynamical features (entropy, complexity, word statistics)
5. Train and evaluate SVM classifier

## Requirements

Core:

- Java (JDK 8 or higher)
- Apache Netbeans 

Optional (for plots and analysis):

- Python 3.x
- MATLAB

