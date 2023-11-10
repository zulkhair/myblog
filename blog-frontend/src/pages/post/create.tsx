import {AddPost} from '@/routes/Post/create';
import LayoutPage from '@/components/Layout/index.view';

export default function AddPostPage() {
    return (
        <LayoutPage title="Create Post" path={['Create Post']}>
            <AddPost/>
        </LayoutPage>
    );
}
