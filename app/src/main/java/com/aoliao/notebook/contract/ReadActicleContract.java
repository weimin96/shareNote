package com.aoliao.notebook.contract;

import com.aoliao.notebook.utils.entity.Comment;
import com.aoliao.notebook.utils.entity.Post;
import com.aoliao.notebook.utils.entity.Reply;
import com.aoliao.notebook.utils.entity.User;
import com.aoliao.notebook.xmvp.XContract;

import java.util.List;


/**
 * 阅读文章协约
 */

public interface ReadActicleContract {
    interface Presenter extends XContract.Presenter {

        /**
         * 通过文章的id查询所有评论
         * @param postId
         */
        void requestCommentsData(String postId);

        /**
         * 获取所有评论的回复
         * @param comments
         */
        void requestCommentsReply(List<Comment> comments);

        /**
         * 请求文章数据
         * @param
         */
        void requestPostData();

        /**
         * 请求喜欢文章
         */
        void requestLike(Post post);
        /**
         * 请求跟随
         */
        void requestFollow(User user);

        /**
         * 添加评论
         * @param comment
         */
        void addComment(String comment);

        void addReply(User replyUser, Comment comment, String content);
    }

    interface View extends XContract.View {
        /**
         * 显示文章
         * @param post
         */
        void showArticle(Post post);

        /**
         * 显示评论
         */
        void showComment(List<Comment> comments, List<Reply> replyList);



        /**
         * 添加评论成功
         */
        void addCommentSuccess();

        /**
         * 添加评论失败
         * @param err
         */
        void addCommentFail(String err);

        /**
         * 回复评论成功
         */
        void replyCommentSuccess();

        /**
         * 回复评论失败
         * @param err
         */
        void replyCommentFail(String err);

        /**
         * 喜欢文章成功
         */
        void likePostSuccess();
        void likePostFail(String err);

        /**
         * 跟随某人成功
         */
        void followUserSuccess();

        /**
         * 跟随某人失败
         * @param err
         */
        void followUserFail(String err);

    }
}
