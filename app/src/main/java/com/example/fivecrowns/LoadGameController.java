package com.example.fivecrowns;


import java.io.File;


class LoadGameController {
    /**
     * Instance of repoService
     */
    private RepoService repoService;

    /**
     * Constructor
     */
    LoadGameController(){
        repoService = RepoService.getInstance();
    }

    /**
     * Loads Game Repo from file
     * @param file File from which game repo is loaded
     */
    void loadRepo(File file){
        repoService.loadGame(file);
    }
}
