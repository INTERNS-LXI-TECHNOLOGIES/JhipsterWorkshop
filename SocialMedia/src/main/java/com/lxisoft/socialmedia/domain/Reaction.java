package com.lxisoft.socialmedia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lxisoft.socialmedia.domain.enumeration.ReactionMode;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Reaction.
 */
@Entity
@Table(name = "reaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "time")
    private Instant time;

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_mode")
    private ReactionMode reactionMode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "posts" }, allowSetters = true)
    private AppUser reactedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "likes", "appUser" }, allowSetters = true)
    private Post post;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTime() {
        return this.time;
    }

    public Reaction time(Instant time) {
        this.setTime(time);
        return this;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public ReactionMode getReactionMode() {
        return this.reactionMode;
    }

    public Reaction reactionMode(ReactionMode reactionMode) {
        this.setReactionMode(reactionMode);
        return this;
    }

    public void setReactionMode(ReactionMode reactionMode) {
        this.reactionMode = reactionMode;
    }

    public AppUser getReactedBy() {
        return this.reactedBy;
    }

    public void setReactedBy(AppUser appUser) {
        this.reactedBy = appUser;
    }

    public Reaction reactedBy(AppUser appUser) {
        this.setReactedBy(appUser);
        return this;
    }

    public Post getPost() {
        return this.post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Reaction post(Post post) {
        this.setPost(post);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reaction)) {
            return false;
        }
        return getId() != null && getId().equals(((Reaction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reaction{" +
            "id=" + getId() +
            ", time='" + getTime() + "'" +
            ", reactionMode='" + getReactionMode() + "'" +
            "}";
    }
}
