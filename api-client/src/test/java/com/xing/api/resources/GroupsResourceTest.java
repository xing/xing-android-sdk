package com.xing.api.resources;

import com.xing.api.HttpError;
import com.xing.api.Response;
import com.xing.api.data.groups.Comment;
import com.xing.api.data.groups.Forum;
import com.xing.api.data.groups.ForumPermission;
import com.xing.api.data.groups.Group;
import com.xing.api.data.groups.MediaPreview;
import com.xing.api.data.groups.Membership;
import com.xing.api.data.groups.Post;
import com.xing.api.data.groups.PostPermission;
import com.xing.api.data.profile.XingUser;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;

import static com.xing.api.TestUtils.file;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test each resource method against a success server response. This test is a minimal safety measure to ensure that each
 * method works as expected. In case if the json response changes on the server side, this test WILL NOT reflect that.
 */
public class GroupsResourceTest extends ResourceTestCase<GroupsResource> {

    public GroupsResourceTest() {
        super(GroupsResource.class);
    }

    @Test
    public void getUsersGroups() throws Exception {
        server.enqueue(new MockResponse().setBody(file("groups/groups.json")));
        Response<List<Group>, HttpError> response = resource.getOwnGroups().execute();
        assertThat(response.body().size()).isEqualTo(1);
    }

    @Test
    public void findGroupsByKeyword() throws Exception {
        server.enqueue(new MockResponse().setBody(file("groups/search_result.json")));
        Response<List<Group>, HttpError> response = resource.findGroupByKeyword("android").execute();
        assertThat(response.body().size()).isEqualTo(10);
        Group firstGroup = response.body().get(0);
        assertThat(firstGroup.closed()).isEqualTo(false);
        assertThat(firstGroup.name()).isEqualTo("Android Community");
    }

    @Test
    public void getForumsOfGroup() throws Exception {
        server.enqueue(new MockResponse().setBody(file("groups/forums.json")));
        Response<List<Forum>, HttpError> response = resource.getForumsOfGroup("123456").execute();
        assertThat(response.body().size()).isEqualTo(7);
        Forum firstForum = response.body().get(0);
        assertThat(firstForum).isNotNull();
        assertThat(firstForum.name()).isEqualTo("Vorstellungsrunde");
        ArrayList<ForumPermission> controlPermissions = new ArrayList<>(2);
        controlPermissions.add(ForumPermission.READ);
        controlPermissions.add(ForumPermission.POST);
        assertThat(firstForum.permissions()).isEqualTo(controlPermissions);
    }

    @Test
    public void getPostsOfForum() throws Exception {
        server.enqueue(new MockResponse().setBody(file("groups/posts.json")));
        Response<List<Post>, HttpError> response = resource.getPostsOfForum("123245").execute();
        assertThat(response.body().size()).isEqualTo(10);
        Post firstPost = response.body().get(0);
        assertThat(firstPost.title()).isEqualTo("UL Test Tool User Group Meeting Munich");
        assertThat(firstPost.permissions().size()).isEqualTo(5);
        assertThat(firstPost.permissions().get(0)).isEqualTo(PostPermission.READ);
    }

    @Test
    public void getPostInGroup() throws Exception {
        server.enqueue(new MockResponse().setBody(file("groups/post_details.json")));
        Response<Post, HttpError> response = resource.getPostInGroup("12345").execute();
        Post post = response.body();
        assertThat(post).isNotNull();
        assertThat(post.title()).isEqualTo("UL Test Tool User Group Meeting Munich");
        XingUser author = new XingUser("14481969_faacc6");
        assertThat(post.author()).isEqualTo(author);
    }

    @Test
    public void getAllPostsOfGroup() throws Exception {
        server.enqueue(new MockResponse().setBody(file("groups/posts_of_group.json")));
        Response<List<Post>, HttpError> response = resource.getAllPostsOfGroup("1234123").execute();
        assertThat(response.body()).isNotNull();
        assertThat(response.body().size()).isEqualTo(10);
        Post firstPost = response.body().get(0);
        assertThat(firstPost).isNotNull();
        assertThat(firstPost.title()).isEqualTo("Android-Entwickler (M/W) - Region Oldenburg (Oldb) - Hamburg");
        assertThat(firstPost.content()).isEqualTo(
              "Android Entwickler (M/W) - Oldenburg (Oldb) oder Hamburg\n"
                    + "\n"
                    + "Nur wenn man etwas wirklich gern macht, macht man es auch wirklich gut. Wir legen daher großen "
                    + "Wert auf eine angenehme Atmosphäre, die es unseren Entwicklern und Beratern erlaubt, das "
                    + "auszuleben, was sie bei all ihrer Vielfalt verbindet: ihre Leidenschaft für Technik, Computer und "
                    + "Programme. Wir bieten eine anspruchsvolle Position mit viel Abwechslung in den Aufgabenstellungen "
                    + "und Kundenprojekten. Für die tägliche Arbeit bekommst Du den nötigen Freiraum, den Du brauchst - "
                    + "und auch interne Verantwortung für Deine eigenen Projekte. Wir arbeiten in einem jungen, "
                    + "aufgeschlossenen Team und modernem Arbeitsumfeld.\n"
                    + "\n"
                    + "Unsere gemeinsamen Aufgaben:\n"
                    + "- Software-Entwicklung für Smartphone- und Tablet-Plattformen\n"
                    + "(iPhone, iPad, Android, Windows-Phone)\n"
                    + "- Programmierung plattformunabhängiger Software\n"
                    + "- Portal- und Website-Entwicklung\n"
                    + "- Technologie-Consulting\n"
                    + "- UI / UX Design\n"
                    + "\n"
                    + "Deine Qualitäten:\n"
                    + "- Erfolgreich abgeschlossenes Studium der Informatik/Wirtschaftsinformatik oder vergleichbare "
                    + "Ausbildung mit entsprechendem Hintergrund\n"
                    + "- Kenntnisse als Entwickler in Android ab Version 4 sowie den gängigen Tools (Android SDK)\n"
                    + "- Erfahrungen im Testen, Releasen und Deployen von Android Apps\n"
                    + "- idealer weise bereits Umgang mit agiler Projektmethode (SCRUM)\n"
                    + "\n"
                    + "Wir sind ein Softwareentwicklungshaus mit Hauptsitz in Oldenburg (Oldb) und einer Niederlassung in"
                    + " Hamburg. Unser Team von 15 Mitmachern entwickelt individuelle Softwareapplikationen, z.B. mobile "
                    + "Anwendungen für iPhone und Android. Viele zufriedene Kunden - insbesondere ein großes regionales "
                    + "Energieunternehmen - profitieren von unserer mehrjährigen Erfahrung. Aufgrund unserer "
                    + "hervorragenden Auftragslage wollen wir weiter wachsen.\n"
                    + "\n"
                    + "Kontaktaufnahme: \n"
                    + "\n"
                    + "Markus Kunle, Personalberater \n"
                    + "expertum GmbH \n"
                    + "01752626532\n"
                    + "markus.kunle@expertum-gruppe.de");
        assertThat(firstPost.likeCount()).isEqualTo(0);
        assertThat(firstPost.commentCount()).isEqualTo(0);
    }

    @Test
    public void getLikersOfPost() throws Exception {
        server.enqueue(new MockResponse().setBody(file("groups/likers.json")));
        Response<List<XingUser>, HttpError> response = resource.getLikersOfPost("123123").execute();
        assertThat(response.body().size()).isEqualTo(6);
        XingUser firstLiker = response.body().get(0);
        assertThat(firstLiker).isNotNull();
        assertThat(firstLiker.id()).isEqualTo("20455280_f9b463");
    }

    @Test
    public void likePost() throws Exception {
        testVoidSpec(resource.likePost("123"));
    }

    @Test
    public void unlikePost() throws Exception {
        testVoidSpec(resource.unlikePost("123"));
    }

    @Test
    public void getCommentsOfPost() throws Exception {
        server.enqueue(new MockResponse().setBody(file("groups/comments.json")));
        Response<List<Comment>, HttpError> response = resource.getCommentsOfPost("123").execute();
        assertThat(response.body().size()).isEqualTo(1);
        Comment firstComment = response.body().get(0);
        assertThat(firstComment).isNotNull();
        assertThat(firstComment.content()).isEqualTo("Hi Dagmar, I take one. Thank you!");
        assertThat(firstComment.userLiked()).isFalse();
        XingUser commentAuthor = new XingUser("7961724_04b13c");
        assertThat(firstComment.author()).isEqualTo(commentAuthor);
    }

    @Test
    public void addCommentToPost() throws Exception {
        testVoidSpec(resource.addCommentToPost("My awesome comment", "1234"));
    }

    @Test
    public void deleteCommentOfPost() throws Exception {
        testVoidSpec(resource.deleteCommentOfPost("1123"));
    }

    @Test
    public void getLikersOfComment() throws Exception {
        server.enqueue(new MockResponse().setBody(file("groups/comment_likers.json")));
        Response<List<XingUser>, HttpError> response = resource.getLikersOfComment("12332").execute();
        assertThat(response.body().size()).isEqualTo(7);
        XingUser firstLiker = response.body().get(0);
        assertThat(firstLiker).isNotNull();
        assertThat(firstLiker.id()).isEqualTo("20177409_08f66a");
    }

    @Test
    public void likeComment() throws Exception {
        testVoidSpec(resource.likeComment("1235"));
    }

    @Test
    public void unlikeComment() throws Exception {
        testVoidSpec(resource.unlikeComment("1235"));
    }

    @Test
    public void markAllPostsAsRead() throws Exception {
        testVoidSpec(resource.markAllPostsAsRead("12345"));
    }

    @Test
    public void joinGroup() throws Exception {
        server.enqueue(new MockResponse().setBody(file("groups/join.json")));
        Response<Membership, HttpError> response = resource.joinGroup("12345").execute();
        assertThat(response).isNotNull();
        assertThat(response.body()).isEqualTo(Membership.MEMBER);
    }

    @Test
    public void createPost() throws Exception {
        server.enqueue(new MockResponse().setBody(file("groups/create_post.json")));
        Response<Post, HttpError> response =
              resource.createPost("My funny Post", "A funny rabbit goes to a hole", "213").execute();
        assertThat(response.body()).isNotNull();
        assertThat(response.body().title()).isEqualTo("My funny Post");
        assertThat(response.body().author().displayName()).isEqualTo("Vladimir");
    }

    @Test
    public void deletePost() throws Exception {
        testVoidSpec(resource.deletePost("1234"));
    }

    @Test
    public void leaveGroup() throws Exception {
        testVoidSpec(resource.leaveGroup("12345"));
    }

    @Test
    public void createMediaPreview() throws Exception {
        server.enqueue(new MockResponse().setBody(file("groups/media_preview.json")));
        Response<MediaPreview, HttpError> response = resource.createMediaPreview("https://dev.xing.com").execute();
        assertThat(response.body()).isNotNull();
        assertThat(response.body().title()).isEqualTo("XING Developer");
        assertThat(response.body().description())
              .isEqualTo("The XING API helps you connect your app with over 14 million users.");
        assertThat(response.body().imageUrl()).isNullOrEmpty();
    }
}
