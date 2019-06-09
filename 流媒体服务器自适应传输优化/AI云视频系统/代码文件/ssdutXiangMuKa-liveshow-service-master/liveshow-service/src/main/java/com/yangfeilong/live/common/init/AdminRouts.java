package com.yangfeilong.live.common.init;

import com.jfinal.config.Routes;
import com.yangfeilong.live.comments.CommentController;
import com.yangfeilong.live.delete.DeleteController;
import com.yangfeilong.live.lock.SecurityLockController;
import com.yangfeilong.live.temp_commment.TempCommentController;
import com.yangfeilong.live.update.UpdateController;
import com.yangfeilong.live.user.live.LiveController;
import com.yangfeilong.live.user.login.LoginController;
import com.yangfeilong.live.upload.UploadController;
import com.yangfeilong.live.user.index.UserIndexController;
import com.yangfeilong.live.user.mesvalidate.MesValidateController;
import com.yangfeilong.live.user.register.RegisterController;
import com.yangfeilong.live.video.comment.VideoCommentController;
import com.yangfeilong.live.video.index.VideoIndexController;

/**
 * Created by gavin on 17-9-20.
 */
public class AdminRouts extends Routes{
    public void config() {
        add("/user", UserIndexController.class);
        add("/video", VideoIndexController.class);
        add("/vcomment", VideoCommentController.class);
        add("/login", LoginController.class);
        add("/upload", UploadController.class);
        add("/delete", DeleteController.class);
        add("/comments", CommentController.class);
        add("/register", RegisterController.class);
        add("/mesvalidate", MesValidateController.class);
        add("/live", LiveController.class);
        add("/temp_comments", TempCommentController.class);
        add("/update", UpdateController.class);
        add("/lock", SecurityLockController.class);
    }
}
