package com.lh.blog.config;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.lh.blog.modules.category.entity.BlogCategory;
import com.lh.blog.modules.category.repository.BlogCategoryRepository;
import com.lh.blog.modules.comment.entity.BlogComment;
import com.lh.blog.modules.comment.repository.BlogCommentRepository;
import com.lh.blog.modules.post.entity.BlogPost;
import com.lh.blog.modules.post.repository.BlogPostRepository;
import com.lh.blog.modules.site.entity.BlogSiteConfig;
import com.lh.blog.modules.site.repository.BlogSiteConfigRepository;
import com.lh.blog.modules.tag.entity.BlogPostTag;
import com.lh.blog.modules.tag.entity.BlogTag;
import com.lh.blog.modules.tag.repository.BlogPostTagRepository;
import com.lh.blog.modules.tag.repository.BlogTagRepository;
import com.lh.blog.modules.user.entity.SysUser;
import com.lh.blog.modules.user.repository.SysUserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SysUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BlogSiteConfigRepository siteConfigRepository;
    private final BlogCategoryRepository categoryRepository;
    private final BlogTagRepository tagRepository;
    private final BlogPostRepository postRepository;
    private final BlogPostTagRepository postTagRepository;
    private final BlogCommentRepository commentRepository;

    public DataInitializer(SysUserRepository userRepository, PasswordEncoder passwordEncoder,
            BlogSiteConfigRepository siteConfigRepository, BlogCategoryRepository categoryRepository,
            BlogTagRepository tagRepository, BlogPostRepository postRepository,
            BlogPostTagRepository postTagRepository, BlogCommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.siteConfigRepository = siteConfigRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
        this.postTagRepository = postTagRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Files.createDirectories(Paths.get("uploads"));
        if (!userRepository.existsByUsername("admin")) {
            SysUser admin = new SysUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("Admin123456"));
            admin.setNickname("站长");
            admin.setEmail("admin@example.com");
            admin.setStatus(1);
            userRepository.save(admin);
        }

        if (siteConfigRepository.count() == 0) {
            BlogSiteConfig config = new BlogSiteConfig();
            config.setSiteName("MyWeb Blog");
            config.setSiteSubtitle("React + Spring Boot 个人博客");
            config.setSiteDescription("一个可直接用于第一阶段开发与演示的个人博客项目。");
            config.setSiteKeywords("博客,React,Spring Boot,MySQL");
            config.setAuthorName("lh");
            config.setAuthorIntro("在这里记录我的开发实践、思考和生活片段。");
            config.setGithubUrl("https://github.com/");
            config.setEmail("admin@example.com");
            config.setAboutMd("# 关于我\n\n欢迎来到我的博客，这里会持续更新技术文章与项目记录。");
            siteConfigRepository.save(config);
        }

        if (categoryRepository.count() == 0 && tagRepository.count() == 0 && postRepository.count() == 0) {
            BlogCategory category = new BlogCategory();
            category.setName("技术随笔");
            category.setSlug("tech-notes");
            category.setDescription("记录技术学习与实践");
            category.setSort(1);
            category.setStatus(1);
            categoryRepository.save(category);

            BlogTag reactTag = new BlogTag();
            reactTag.setName("React");
            reactTag.setSlug("react");
            reactTag.setColor("#1f7ae0");
            BlogTag springTag = new BlogTag();
            springTag.setName("Spring Boot");
            springTag.setSlug("spring-boot");
            springTag.setColor("#2b8a3e");
            tagRepository.saveAll(Arrays.asList(reactTag, springTag));

            BlogPost welcome = new BlogPost();
            welcome.setTitle("欢迎来到我的个人博客");
            welcome.setSlug("welcome-my-blog");
            welcome.setSummary("这是项目初始化后的第一篇示例文章，你可以在后台继续编辑和扩展它。");
            welcome.setContentMd("# 欢迎来到我的个人博客\n\n这是博客系统的第一篇文章。\n\n- 前端使用 React\n- 后端使用 Spring Boot\n- 数据库使用 MySQL\n\n接下来你可以在后台继续发布更多内容。\n");
            welcome.setCategoryId(category.getId());
            welcome.setStatus(1);
            welcome.setIsTop(1);
            welcome.setAllowComment(1);
            welcome.setPublishedAt(LocalDateTime.now());
            welcome.setSeoTitle("欢迎来到我的个人博客");
            welcome.setSeoDescription("第一篇博客文章，介绍整个博客系统的技术栈与后续方向。");
            postRepository.save(welcome);

            BlogPostTag relation1 = new BlogPostTag();
            relation1.setPostId(welcome.getId());
            relation1.setTagId(reactTag.getId());
            BlogPostTag relation2 = new BlogPostTag();
            relation2.setPostId(welcome.getId());
            relation2.setTagId(springTag.getId());
            postTagRepository.saveAll(Arrays.asList(relation1, relation2));

            BlogComment comment = new BlogComment();
            comment.setPostId(welcome.getId());
            comment.setParentId(0L);
            comment.setNickname("第一位访客");
            comment.setEmail("visitor@example.com");
            comment.setContent("项目骨架看起来很稳，期待后续文章更新。");
            comment.setStatus(1);
            commentRepository.save(comment);
        }
    }
}