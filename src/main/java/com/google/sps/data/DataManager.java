// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

public interface DataManager {
  public void storeUser(User user);
  public void updateUser(User user);
	public User retrieveUser(String userID);

	public void storeGame(Game game);
	public void updateGame(Game game);
	public Game retrieveGame(String gameID);

	public void storeStage(Stage stage);
  public void updateStage(Stage stage);
	public Stage retrieveStage(String stageID);

	public void storeProgress(Progress progress);
	public void updateProgress(Progress progress);
	public Progress retrieveProgress(String progressID);
}
