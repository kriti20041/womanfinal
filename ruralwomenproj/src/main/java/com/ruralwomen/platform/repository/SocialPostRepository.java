package com.ruralwomen.platform.repository;

import com.ruralwomen.platform.model.SocialPost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialPostRepository extends MongoRepository<SocialPost, String> {
}
