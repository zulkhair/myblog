import {EditPost} from '@/routes/Post/edit';
import LayoutPage from '@/components/Layout/index.view';
import {useRouter} from "next/router";

export default function EditPostPage() {
    const router = useRouter();
    const {postId} = router.query;

    if (postId === undefined || Array.isArray(postId)) {
        return <div>Invalid post id provided</div>;
    }

    return (
        <LayoutPage title="Edit Post" path={['Post', postId]}>
            <EditPost postId={postId}/>
        </LayoutPage>
    );
}
