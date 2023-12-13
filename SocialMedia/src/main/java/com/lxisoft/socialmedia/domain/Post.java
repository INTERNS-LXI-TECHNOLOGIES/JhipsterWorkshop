package com.lxisoft.socialmedia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Post.
 */
@Entity
@Table(name = "post")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at")
    private Instant createdAt;

    @Size(max = 1024)
    @Column(name = "content", length = 1024)
    private String content;

    @Column(name = "hash_tags")
    private String hashTags;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reactedBy", "post" }, allowSetters = true)
    private Set<Reaction> likes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "posts" }, allowSetters = true)
    private AppUser appUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Post id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Post createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return this.content;
    }

    public Post content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHashTags() {
        return this.hashTags;
    }

    public Post hashTags(String hashTags) {
        this.setHashTags(hashTags);
        return this;
    }

    public void setHashTags(String hashTags) {
        this.hashTags = hashTags;
    }

    public Set<Reaction> getLikes() {
        return this.likes;
    }

    public void setLikes(Set<Reaction> reactions) {
        if (this.likes != null) {
            this.likes.forEach(i -> i.setPost(null));
        }
        if (reactions != null) {
            reactions.forEach(i -> i.setPost(this));
        }
        this.likes = reactions;
    }

    public Post likes(Set<Reaction> reactions) {
        this.setLikes(reactions);
        return this;
    }

    public Post addLike(Reaction reaction) {
        this.likes.add(reaction);
        reaction.setPost(this);
        return this;
    }

    public Post removeLike(Reaction reaction) {
        this.likes.remove(reaction);
        reaction.setPost(null);
        return this;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Post appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Post)) {
            return false;
        }
        return getId() != null && getId().equals(((Post) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Post{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", content='" + getContent() + "'" +
            ", hashTags='" + getHashTags() + "'" +
            "}";
    }
}
