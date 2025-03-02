package ru.practicum.myblog.controllers;

public interface BlogUrls {
    String ROOT = "";

    interface Posts {
        String PART = "posts";
        String FULL = ROOT + "/" + PART;

        interface Filter {
            String PART = "filter";
            String FULL = Posts.FULL + "/" + PART;

            interface Reset {
                String PART = "reset";
                String FULL = Filter.FULL + "/" + PART;
            }
        }

        interface PostId {
            String PART = "{postId}";
            String FULL = Posts.FULL + "/" + PART + "/";

            interface Delete {
                String PART = "delete";
                String FULL = PostId.FULL + PART;
            }

            interface Like {
                String PART = "like";
                String FULL = PostId.FULL + PART;
            }

            interface Comments {
                String PART = "comments";
                String FULL = PostId.FULL + PART;

                interface Edit {
                    String PART = "edit";
                    String FULL = Comments.FULL + "/" + PART;
                }
            }
        }

        interface Editor {
            String PART = "editor";
            String FULL = Posts.FULL + "/" + PART;
        }
    }
}
