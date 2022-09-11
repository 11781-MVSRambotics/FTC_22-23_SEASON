# Introduction

This repo contains the most recent stable version of our code for the upcoming Powerplay season.

This is just a fork of the FtcRobotController, so if you want to go see the original readme, see the upstream repo.

For our team, here are the basic rules for how we are gonna manage this repo:

## Feature-branch workflow:

We are gonna be using a feature-branch workflow. This just means that whenever you want to add a new "feature," you have to create a new branch. I'll put a basic step-by-step here for reference. I won't cover actual programming steps here, just repo management rules. [Here](https://gist.github.com/blackfalcon/8428401) is a much more thorough walkthrough of how this workflow works. Also, it goes without saying that everyone needs Git installed on their machine before they can work on this. Git is separate from Github and needs to be installed on its own.
	
1. Pull the most recent commits from master so you are up to date with the remote repository

2. Create a local branch with a name indicative of the feature you are implementing

3. Checkout the new feature branch and begin your work in Android Studio

4. When you've tested and confirmed that it works, sync your local repo with origin/master again

5. Merge the feature branch into your, now updated, local master and resolve any conflicts

6. Push your merged master branch to the remote branch

7. Delete local feature-branch
	
It's VERY important that, when we are working on the code, we make absolutely certain that we have our local master branch up to date before we start and before we push any changes. If we don't do this, we will inevitably overwrite other peoples changes as well as generally having far more conflicts across the project. The reason this is so critical is that we will not be doing pull requests simply because we've got a small team and nobody wants to get micro-managed. Just understand that this means whoever is programming the feature is responsible for making it fit into everything else. NEVER fix conflicts by telling github to use all of your changes before checking it thoroughly first. 
	
## Methods for using Git:

There's no specific way you have to go about using Git. The only rule here is that you absolutely must use it if you want to program the robot. No exceptions. As for actual methods, there are three viable options for our use case.
	
### Git with a graphical user interface:
		
Git has lots of GUI tools that you can utilize. There are two easy ones that I'm familier with that I'll recommend here. Those being Github Desktop, the official tool for linking with Github, and Android Studio's intigrated Git UI. As far as I know, either of these tools can accomplish everything we need to manage our code for this season. If you plan to do it all from Android Studio, you don't need to install anything. Github Desktop is an actual installed application you can download from Github's official page. 
		
### Git from the command line:

The other option for Git is to use the command line interface. This is definitly the more andvanced option if you're not super familier with working in a text box, but you'll get more control over your project as a result. This is the one I'll be using as I am running Linux and it's just easier for me to manage that way. If you're running a Windows machine and want to utilize the command line, you will need to install Git Bash, which is a terminal enviornment for issuing Git commands. That's a must if you choose to operate this way. That tool is available from Git's website, not Github, so don't be weirded out if the website doesn't look like the Github one.
		
Just to clairify, if you've got another tool for using Git that you prefer or want to try, that's perfectly fine. As long as it works and allows us to keep our code in sync then it's up to preference. 

## Other miscellaneous things:

This repository is a fork (copy) of the FtcRobotController repository that FIRST releases. Doing it this way makes it easier to sync up with that repo when FIRST releases updates for it. If you're on the remote repository on Github's website, you'll see two options near the top that say "Contribute" and "Sync Fork". DO NOT DO EITHER OF THESE THINGS EVER. Contribute will open a pull request for FIRST to merge our code into the official repo, which will confuse and likely annoy them honestly. Sync Fork is something we will have to do, but there could potentially be destructive conflicts so I'll oversee that if it becomes neccessary for us to update our fork. 

If you're not already on the Discord, and I know some of you aren't, I'll put the link [here](https://discord.gg/E8jsaMf2SU) so you can access it cause that's the easiest way for us to communicated about robotics things if you miss meetings. Also there's a webhook bot thing in the code channel that will tell you what's up with the Github repo. If you're reading this and not on the team, feel free to join if you have questions you wanna ask.

It's pretty easy to catastrophically screw up a Github repository and it's usually quite a hassle to fix. Hopefully this readme, if you actually read it, will equip you with the tools to not royally screw up the repo. However, like I said, it's not hard and I've done it myself so if something happens definitly don't try to hide it cause that makes it a thousand times worse. Just let me know and we can resolve the issue.
