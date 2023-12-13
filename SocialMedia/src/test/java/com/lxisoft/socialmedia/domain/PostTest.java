package com.lxisoft.socialmedia.domain;

import static com.lxisoft.socialmedia.domain.AppUserTestSamples.*;
import static com.lxisoft.socialmedia.domain.PostTestSamples.*;
import static com.lxisoft.socialmedia.domain.ReactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lxisoft.socialmedia.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PostTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Post.class);
        Post post1 = getPostSample1();
        Post post2 = new Post();
        assertThat(post1).isNotEqualTo(post2);

        post2.setId(post1.getId());
        assertThat(post1).isEqualTo(post2);

        post2 = getPostSample2();
        assertThat(post1).isNotEqualTo(post2);
    }

    @Test
    void likeTest() throws Exception {
        Post post = getPostRandomSampleGenerator();
        Reaction reactionBack = getReactionRandomSampleGenerator();

        post.addLike(reactionBack);
        assertThat(post.getLikes()).containsOnly(reactionBack);
        assertThat(reactionBack.getPost()).isEqualTo(post);

        post.removeLike(reactionBack);
        assertThat(post.getLikes()).doesNotContain(reactionBack);
        assertThat(reactionBack.getPost()).isNull();

        post.likes(new HashSet<>(Set.of(reactionBack)));
        assertThat(post.getLikes()).containsOnly(reactionBack);
        assertThat(reactionBack.getPost()).isEqualTo(post);

        post.setLikes(new HashSet<>());
        assertThat(post.getLikes()).doesNotContain(reactionBack);
        assertThat(reactionBack.getPost()).isNull();
    }

    @Test
    void appUserTest() throws Exception {
        Post post = getPostRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        post.setAppUser(appUserBack);
        assertThat(post.getAppUser()).isEqualTo(appUserBack);

        post.appUser(null);
        assertThat(post.getAppUser()).isNull();
    }
}
