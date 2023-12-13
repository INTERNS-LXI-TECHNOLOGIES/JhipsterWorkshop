package com.lxisoft.socialmedia.domain;

import static com.lxisoft.socialmedia.domain.AppUserTestSamples.*;
import static com.lxisoft.socialmedia.domain.PostTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lxisoft.socialmedia.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AppUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppUser.class);
        AppUser appUser1 = getAppUserSample1();
        AppUser appUser2 = new AppUser();
        assertThat(appUser1).isNotEqualTo(appUser2);

        appUser2.setId(appUser1.getId());
        assertThat(appUser1).isEqualTo(appUser2);

        appUser2 = getAppUserSample2();
        assertThat(appUser1).isNotEqualTo(appUser2);
    }

    @Test
    void postTest() throws Exception {
        AppUser appUser = getAppUserRandomSampleGenerator();
        Post postBack = getPostRandomSampleGenerator();

        appUser.addPost(postBack);
        assertThat(appUser.getPosts()).containsOnly(postBack);
        assertThat(postBack.getAppUser()).isEqualTo(appUser);

        appUser.removePost(postBack);
        assertThat(appUser.getPosts()).doesNotContain(postBack);
        assertThat(postBack.getAppUser()).isNull();

        appUser.posts(new HashSet<>(Set.of(postBack)));
        assertThat(appUser.getPosts()).containsOnly(postBack);
        assertThat(postBack.getAppUser()).isEqualTo(appUser);

        appUser.setPosts(new HashSet<>());
        assertThat(appUser.getPosts()).doesNotContain(postBack);
        assertThat(postBack.getAppUser()).isNull();
    }
}
