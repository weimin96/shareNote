

package com.aoliao.notebook.utils;



import android.util.Log;

import com.aoliao.notebook.R;
import com.aoliao.notebook.factory.ErrMsgFactory;
import com.aoliao.notebook.AppController;
import com.aoliao.notebook.utils.entity.BannerData;
import com.aoliao.notebook.utils.entity.Comment;
import com.aoliao.notebook.utils.entity.Post;
import com.aoliao.notebook.utils.entity.Reply;
import com.aoliao.notebook.utils.entity.User;


import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 网络请求
 */
public class NetRequest {
    private static NetRequest mNetRequest;

    private NetRequest() {
    }

    public static NetRequest Instance() {
        if (mNetRequest == null) {
            synchronized (NetRequest.class) {
                mNetRequest = new NetRequest();
            }
        }
        return mNetRequest;
    }

    /**
     * 请求获取用户粉丝数量
     */
    public void requestFansNum(User user, final RequestListener<String> listener) {
        BmobQuery<User> query = new BmobQuery<User>();
        BmobQuery<User> innerQuery = new BmobQuery<>();
        innerQuery.addWhereEqualTo("objectId", user.getObjectId());
//        query.addQueryKeys("followPerson");
        query.addWhereMatchesQuery("followPerson", "_User", innerQuery);
        query.count(User.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    listener.success(Integer.toString(integer));
                } else {
                    listener.success("0");
                }
            }
        });
    }


    /**
     * 请求获取用户粉丝
     */
    public void requestFans(User user, final RequestListener<List<User>> listener) {
        if (!checkNet(listener)) return;
        BmobQuery<User> query = new BmobQuery<User>();
        BmobQuery<User> innerQuery = new BmobQuery<>();
        innerQuery.addWhereEqualTo("objectId", user.getObjectId());
//        query.addQueryKeys("followPerson");
        query.addWhereMatchesQuery("followPerson", "_User", innerQuery);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    listener.success(list);
                } else {
                    listener.error(ErrMsgFactory.errMSG(e.getErrorCode()));
                }
            }
        });
    }

    /**
     * 请求获取用户关注其他用户数量
     */
    public void requestFocusNum(User user, final RequestListener<String> listener) {
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereRelatedTo("followPerson", new BmobPointer(user));
        query.count(User.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    listener.success(Integer.toString(integer));
                } else {
                    listener.success("0");
                }
            }
        });
    }

    /**
     * 请求获取用户关注其他用户
     */
    public void requestFocus(User user, final RequestListener<List<User>> listener) {
        if (!checkNet(listener)) return;
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereRelatedTo("followPerson", new BmobPointer(user));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    listener.success(list);
                } else {
                    listener.error(ErrMsgFactory.errMSG(e.getErrorCode()));
                }
            }
        });
    }

    /**
     * 请求获取用户喜欢的文章数量
     */
    public void requestCollectNum(User user, final RequestListener<String> listener) {
        BmobQuery<Post> query = new BmobQuery<>();
        BmobQuery<User> innerQuery = new BmobQuery<>();
        innerQuery.addWhereEqualTo("objectId", user.getObjectId());
//        query.addQueryKeys("followPerson");
        query.addWhereMatchesQuery("likes", "_User", innerQuery);
        query.count(Post.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    listener.success(Integer.toString(integer));
                } else {
                    listener.success("0");
                }
            }
        });
    }

    /**
     * 跟随该用户
     * @param user
     * @param listener
     */
    public void followUser(final User user, final RequestListener<String> listener) {
        if (!checkNet(listener)) return;
        if (!checkLoginStatus(listener)) return;
        final User currentUser = BmobUser.getCurrentUser(User.class);
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("followPerson", new BmobPointer(user));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e != null) return;
                for (User u : list) {
                    if (u.getObjectId().equals(currentUser.getObjectId())) {
                        stopFollowRequest(user, listener);
                        listener.success("不再关注");
                        return;
                    }
                }
                startFollowRequest(user, listener);
                listener.success("已经关注");
            }
        });

    }

    private void startFollowRequest(User user, final RequestListener<String> listener) {
        final User currentUser = BmobUser.getCurrentUser(User.class);
        BmobRelation relation = new BmobRelation();
        relation.add(user);
        currentUser.setFollowPerson(relation);
        currentUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    listener.success(null);
                } else {
                    listener.error(ErrMsgFactory.errMSG(e.getErrorCode()));
                }
            }
        });
    }

    private void stopFollowRequest(User user, final RequestListener<String> listener) {
        final User currentUser = BmobUser.getCurrentUser(User.class);
        BmobRelation relation = new BmobRelation();
        relation.remove(user);
        currentUser.setFollowPerson(relation);
        currentUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    listener.success(null);
                } else {
                    listener.error(ErrMsgFactory.errMSG(e.getErrorCode()));
                }
            }
        });
    }

    /**
     * 删除发表的帖子
     */
    public void deleteArticle(final Post post, final RequestListener<String> listener) {
        if (!checkNet(listener)) return;
        if (!checkLoginStatus(listener)) return;
        post.delete(post.getObjectId(),new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    listener.success(null);
                }else{
                    listener.error(ErrMsgFactory.errMSG(e.getErrorCode()));
                }
            }
        });
    }

    /**
     * 喜欢文章
     *
     * @param post
     * @param listener
     */
    public void likeComment(final Post post, final RequestListener<String> listener) {
        if (!checkNet(listener)) return;
        if (!checkLoginStatus(listener)) return;
        final User user = BmobUser.getCurrentUser(User.class);
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereRelatedTo("likes", new BmobPointer(post));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e != null) return;
                for (User u : list) {
                    if (u.getObjectId().equals(user.getObjectId())) {
                        removeLikeRequest(post, listener);
                        listener.success("取消收藏成功");
                        return;
                    }
                }
                startLikeRequest(post, listener);
            }
        });

    }

    private void startLikeRequest(Post post, final RequestListener<String> listener) {
        final User user = BmobUser.getCurrentUser(User.class);
        //将当前用户添加到Post表中的likes字段值中，表明当前用户喜欢该帖子
        BmobRelation relation = new BmobRelation();
        //将当前用户添加到多对多关联中
        relation.add(user);
        //多对多关联指向`post`的`likes`字段
        post.setLikes(relation);
        post.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    listener.success("收藏成功");
                } else {
                    listener.error(ErrMsgFactory.errMSG(e.getErrorCode()));
                }
            }
        });
    }

    private void removeLikeRequest(Post post, final RequestListener<String> listener) {
        final User user = BmobUser.getCurrentUser(User.class);
        //将当前用户添加到Post表中的likes字段值中，表明当前用户喜欢该帖子
        BmobRelation relation = new BmobRelation();
        //将当前用户去除到多对多关联中
        relation.remove(user);
        //多对多关联指向`post`的`likes`字段
        post.setLikes(relation);
        post.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    listener.success(null);
                } else {
                    listener.error(ErrMsgFactory.errMSG(e.getErrorCode()));
                }
            }
        });
    }

    /**
     * 添加评论
     */
    public void addComment(Post post, String content, final RequestListener<String> listener) {
        if (!checkNet(listener)) return;
        if (!checkLoginStatus(listener)) return;
        User user = BmobUser.getCurrentUser(User.class);
        final Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        comment.setUser(user);
        comment.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    listener.success(AppController.getAppContext().getString(R.string.add_comment_success));
                } else {
                    listener.error(ErrMsgFactory.errMSG(e.getErrorCode()));
                }
            }

        });
    }

    /**
     * 回复评论
     */
    public void replyComment(User replyUser, Comment comment, String content, final RequestListener listener) {
        if (!checkNet(listener)) return;
        if (!checkLoginStatus(listener)) return;
        Reply reply = new Reply();
        reply.setSpeakUser(BmobUser.getCurrentUser(User.class));
        reply.setComment(comment);
        reply.setContent(content);
        reply.setReplyUser(replyUser);
        reply.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    listener.success(null);
                } else {
                    listener.error(ErrMsgFactory.errMSG(e.getErrorCode()));
                }
            }
        });
    }

    /**
     * 获取所有回复
     * @param comments
     * @param listener
     */
    public void getCommentReply(final List<Comment> comments, final RequestListener<List<Reply>> listener) {
        if (!checkNet(listener)) return;
        final List<Reply> replyList = new ArrayList<>();
        final int numTotal = comments.size();
        nowNum = 0;
        Observable.from(comments)
                .flatMap(new Func1<Comment, Observable<List<Reply>>>() {
                    @Override
                    public Observable<List<Reply>> call(Comment comment) {
                        BmobQuery<Reply> query = new BmobQuery<Reply>();
                        query.addWhereEqualTo("comment", new BmobPointer(comment));
                        query.include("speakUser,replyUser");
                        return query.findObjectsObservable(Reply.class);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Reply>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(List<Reply> replies) {
                        replyList.addAll(replies);
                        nowNum++;
                        if (numTotal==nowNum) {
                                listener.success(replyList);
                            }
                    }
                });
    }

    private int nowNum = 0;

    /**
     * 获取文章的评论
     */
    public void getPostComment(String postId, final RequestListener<List<Comment>> listener) {
        if (!checkNet(listener)) return;
        BmobQuery<Comment> query = new BmobQuery<>();
        Post post = new Post();
        post.setObjectId(postId);
        query.addWhereEqualTo("post", new BmobPointer(post));
        query.include("user");
        query.order("-createdAt");
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if (e == null) {
                    listener.success(list);
                }
            }
        });
    }

    /**
     * 获取广告信息
     *
     * @param listener
     */
    public void pullBannerData(final RequestListener<List<BannerData>> listener) {
        if (!checkNet(listener)) return;
        BmobQuery<BannerData> query = new BmobQuery<>();
        query.findObjects(new FindListener<BannerData>() {
            @Override
            public void done(List<BannerData> list, BmobException e) {
                if (e == null) {
                    listener.success(list);
                } else {
                    listener.error(ErrMsgFactory.errMSG(e.getErrorCode()));
                }
            }
        });

    }

    /**
     * 获取该用户发布的帖子
     * @param currentIndex
     * @param size
     * @param listener
     */
    public void pullReleasePost(String userId,int currentIndex, int size, final RequestListener<List<Post>> listener) {
        if (!checkNet(listener)) return;
        BmobQuery<Post> query = new BmobQuery<>();

        query.addWhereEqualTo("author", userId);    // 查询当前用户的所有帖子
        query.setSkip(currentIndex);
        query.setLimit(size);
        query.order("-updatedAt");
        query.include("author");// 希望在查询帖子信息的同时也把发布人的信息查询出来
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    listener.success(list);
                } else {
                    listener.error(ErrMsgFactory.errMSG(e.getErrorCode()));
                }
            }

        });
    }

    /**
     * 获取收藏/喜欢的帖子
     * @param currentIndex
     * @param size
     * @param listener
     */
    public void pullCollectPost(String userId, int currentIndex, int size, final RequestListener<List<Post>> listener) {
        if (!checkNet(listener)) return;
        BmobQuery<Post> query = new BmobQuery<>();
        BmobQuery<User> innerQuery = new BmobQuery<>();
        innerQuery.addWhereEqualTo("objectId", userId);
        query.addWhereMatchesQuery("likes", "_User", innerQuery);
        query.setSkip(currentIndex);
        query.setLimit(size);
        query.order("-createdAt");
        query.include("author");
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    listener.success(list);
                } else {
                    listener.error(ErrMsgFactory.errMSG(e.getErrorCode()));
                }
            }
        });
    }

    /**
     * 获取帖子数据集合
     *
     * @param size 获取的数据条数
     * @return
     */
    public void pullPostList(int currentIndex, int size, final RequestListener<List<Post>> listener) {
        if (!checkNet(listener)) return;
        BmobQuery<Post> query = new BmobQuery<>();
        query.setSkip(currentIndex);
        query.setLimit(size);
        query.order("-createdAt");
        query.include("author");
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    listener.success(list);
                } else {
                    listener.error(ErrMsgFactory.errMSG(e.getErrorCode()));
                }
            }
        });
    }

    public void queryPost(String objectId,final RequestListener<Post> listener){
        if (!checkNet(listener)) return;
        BmobQuery<Post> query = new BmobQuery<>();
        query.getObject(objectId, new QueryListener<Post>() {
            @Override
            public void done(Post post, BmobException e) {
                if (e == null) {
                    listener.success(post);
                } else {
                    listener.error(ErrMsgFactory.errMSG(e.getErrorCode()));
                }
            }
        });
    }

    /**
     * 上传发表帖子
     *
     * @param coverPicture
     * @param title
     * @param article
     */
    public void uploadArticle(String coverPicture, String title, String article, final RequestListener<String> listener) {
        if (!checkNet(listener)) return;
        if (!checkLoginStatus(listener)) return;
        User user = BmobUser.getCurrentUser(User.class);
        if (user == null) {
            listener.error(AppController.getAppContext().getString(R.string.please_login));
            return;
        }
        Post post = new Post();
        post.setContent(article);
        post.setTitle(title);
        post.setCoverPicture(coverPicture);
        post.setAuthor(user);
        post.save(new SaveListener<String>() {

            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    listener.success(s);
                } else {
                    listener.error(ErrMsgFactory.errMSG(e.getErrorCode()));
                }
            }
        });
    }

    /**
     * 保存文章
     * @param coverPicture
     * @param title
     * @param article
     */
    public void saveArticle(String coverPicture, String title, String article, final RequestListener<String> listener) {
        if (!checkNet(listener)) return;
        if (!checkLoginStatus(listener)) return;
        User user = BmobUser.getCurrentUser(User.class);
        if (user == null) {
            listener.error(AppController.getAppContext().getString(R.string.please_login));
            return;
        }
        Post note = new Post();
        note.setContent(article);
        note.setTitle(title);
//        note.setCoverPicture(coverPicture);
        note.setAuthor(user);
        note.save(new SaveListener<String>() {

            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    listener.success(s);
                } else {
                    listener.error(ErrMsgFactory.errMSG(e.getErrorCode()));
                }
            }
        });
    }

    /**
     * 注册
     * @param user
     */
    public void userRegister(User user, final RequestListener<User> listener) {
        if (!checkNet(listener)) return;
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    listener.success(user);
                } else {
                    listener.error(ErrMsgFactory.errMSG(e.getErrorCode()));
                }
            }
        });
    }

    /**
     * 登录
     * @param username
     * @param password
     * @param listener
     */
    public void login(String username, String password, final RequestListener<User> listener) {
        if (!checkNet(listener)) return;
        BmobUser.loginByAccount(username, MD5Util.getMD5(password), new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    listener.success(user);
                } else {
                    listener.error(ErrMsgFactory.errMSG(e.getErrorCode()));
                }
            }
        });
    }

    /**
     * 退出登录
     */
    public void exitLogin() {
        BmobUser.logOut();   //清除缓存用户对象
    }

    /**
     * 编辑签名
     *
     * @param sign
     * @param listener
     */
    public void editSign(String sign, final RequestListener<String> listener) {
        User user = BmobUser.getCurrentUser(User.class);
        user.setSign(sign);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    listener.success("");
                } else {
                    listener.error(ErrMsgFactory.errMSG(e.getErrorCode()));
                }
            }
        });
    }

    /**
     * 上传图片
     *
     * @param file
     * @param listener
     */
    public void uploadPic(File file, final UploadPicListener<String> listener) {
        if (!checkNet(listener)) return;
        if (!checkLoginStatus(listener)) return;
        listener.compressingPic();
        Compressor.getDefault(AppController.getAppContext())
                .compressToFileAsObservable(file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        listener.compressedPicSuccess();
                        final BmobFile bmobFile = new BmobFile(file);
                        bmobFile.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    listener.success(bmobFile.getFileUrl());
                                } else {
                                    listener.error(ErrMsgFactory.errMSG(e.getErrorCode()));
                                }
                            }

                            @Override
                            public void onProgress(Integer value) {
                                super.onProgress(value);
                                listener.progress(value);
                            }
                        });
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        listener.error(throwable.toString());
                    }
                });
    }

    /**
     * 上传头像
     *
     * @param file
     * @param listener
     */
    public void uploadHeadPic(File file, final RequestListener<String> listener) {
        if (!checkNet(listener)) return;
        final BmobFile bmobFile = new BmobFile(file);
        final User newUser = new User();
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    newUser.setHeadPic(bmobFile.getFileUrl());
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                listener.success(newUser.getHeadPic());
                            } else {
                                listener.error(ErrMsgFactory.errMSG(e.getErrorCode()));
                            }
                        }
                    });
                } else {
                    listener.error(ErrMsgFactory.errMSG(e.getErrorCode()));
                }

            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }

    /**
     * 更新用户信息
     *
     * @param type
     * @param info
     * @param listener
     * @param <T>
     */
    public <T> void updateInfo(UpdateType type, T info, final RequestListener<User> listener) {
        if (!checkNet(listener)) return;
        final User newUser = new User();
        User user = BmobUser.getCurrentUser(User.class);
        int sex=user.getSex();
        newUser.setSex(sex);
        switch (type) {
            case NICKNAME:
                newUser.setNickname((String) info);
                break;
            case BIRTHDAY:
                newUser.setBirthday((Date) info);
                break;
            case SEX:
                newUser.setSex((Integer) info);
                break;
            case PHONE:
                newUser.setMobilePhoneNumber((String) info);
                break;
            case CITY:
                newUser.setCity((String) info);
                break;
            case EMAIL:
                newUser.setEmail((String) info);
                break;
            case SIGN:
                newUser.setSign((String) info);
                break;
            default:
                throw new RuntimeException("UpdateType don't have " + type);
        }
        BmobUser bmobUser = BmobUser.getCurrentUser();
        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    listener.success(newUser);
                } else {
                    listener.error(ErrMsgFactory.errMSG(e.getErrorCode()));
                }
            }
        });
    }

    /**
     * 更新密码
     *
     * @param oldPwd
     * @param newPwd
     * @param listener
     */
    public void updatePassword(String oldPwd, String newPwd, final RequestListener<User> listener) {
        if (!checkNet(listener)) return;
        BmobUser.updateCurrentUserPassword(MD5Util.getMD5(oldPwd), MD5Util.getMD5(newPwd), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    listener.success(null);
                } else {
                    listener.error(ErrMsgFactory.errMSG(e.getErrorCode()));
                }
            }
        });
    }


    /**
     * 更新类型
     */
    public enum UpdateType {
        NICKNAME,
        BIRTHDAY,
        SEX,
        PHONE,
        CITY,
        EMAIL,
        SIGN
    }

    public interface RequestListener<T> {
        void success(T t);

        void error(String err);
    }

    public interface ProgressListener<T> extends RequestListener<T> {
        void progress(int i);
    }

    public interface UploadPicListener<T> extends ProgressListener<T> {
        void compressingPic();

        void compressedPicSuccess();
    }

    public interface ThirdLoginLister<T> extends RequestListener<T> {
        void thirdLoginSuccess();
    }

    /**
     * 检测是否登陆
     */
    public boolean checkLoginStatus(RequestListener listener) {
        if (BmobUser.getCurrentUser(User.class) == null) {
            listener.error("您没有登陆哦！");
            return false;
        }
        return true;
    }

    /**
     * 检测网络
     * @param listener
     * @return
     */
    private boolean checkNet(RequestListener listener) {
        if (NetCheck.isConnected(AppController.getAppContext())) {
            return true;
        } else {
            listener.error(AppController.getAppContext().getString(R.string.please_check_network_status));
            return false;
        }
    }
}
