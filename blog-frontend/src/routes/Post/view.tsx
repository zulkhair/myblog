import PostTableComponent from '@/routes/Post/components/Table/TablePost';
import {mgrnBtm16, pdgRight8} from '@/routes/Post/style';
import {Button} from 'antd';
import Link from 'next/link';

export const PostPage = () => {

    return (
        <div>
            <div className={pdgRight8}>
                <div className={mgrnBtm16}>
                    <Link href={'/post/create'} passHref>
                        <Button type="primary">Create Post</Button>
                    </Link>
                </div>
                <div>
                    <div>
                        <PostTableComponent/>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default PostPage;
