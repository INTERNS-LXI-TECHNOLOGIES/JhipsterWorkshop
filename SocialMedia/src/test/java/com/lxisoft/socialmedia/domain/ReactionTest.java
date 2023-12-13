package com.lxisoft.socialmedia.domain;

import static com.lxisoft.socialmedia.domain.AppUserTestSamples.*;
import static com.lxisoft.socialmedia.domain.PostTestSamples.*;
import static com.lxisoft.socialmedia.domain.ReactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.lxisoft.socialmedia.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reaction.class);
        Reaction reaction1 = getReactionSample1();
        Reaction reaction2 = new Reaction();
        assertThat(reaction1).isNotEqualTo(reaction2);

        reaction2.setId(reaction1.getId());
        assertThat(reaction1).isEqualTo(reaction2);

        reaction2 = getReactionSample2();
        assertThat(reaction1).isNotEqualTo(reaction2);
    }

    @Test
    void reactedByTest() throws Exception {
        Reaction reaction = getReactionRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        reaction.setReactedBy(appUserBack);
        assertThat(reaction.getReactedBy()).isEqualTo(appUserBack);

        reaction.reactedBy(null);
        assertThat(reaction.getReactedBy()).isNull();
    }

    @Test
    void postTest() throws Exception {
        Reaction reaction = getReactionRandomSampleGenerator();
        Post postBack = getPostRandomSampleGenerator();

        reaction.setPost(postBack);
        assertThat(reaction.getPost()).isEqualTo(postBack);

        reaction.post(null);
        assertThat(reaction.getPost()).isNull();
    }
}
