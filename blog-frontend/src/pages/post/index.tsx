import LayoutPage from '@/components/Layout/index.view';
import PostPage from '@/routes/Post/view';

export default function ViewPostPage() {
    return (
        <LayoutPage title="Post" path={['Post']}>
            <PostPage/>
        </LayoutPage>
    );
}
