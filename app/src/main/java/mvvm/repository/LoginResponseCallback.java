package mvvm.repository;

import mvvm.model.User;

public interface LoginResponseCallback {
    void onAsyncCallFinished(User result);
}
